/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.tagger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.trainrobots.Log;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Terminal;
import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.tokenizer.Tokenizer;
import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Treebank;

import eu.danieldk.nlp.jitar.corpus.TaggedWord;
import eu.danieldk.nlp.jitar.data.BiGram;
import eu.danieldk.nlp.jitar.data.Model;
import eu.danieldk.nlp.jitar.data.TriGram;
import eu.danieldk.nlp.jitar.data.UniGram;
import eu.danieldk.nlp.jitar.languagemodel.LanguageModel;
import eu.danieldk.nlp.jitar.languagemodel.LinearInterpolationLM;
import eu.danieldk.nlp.jitar.tagger.HMMTagger;
import eu.danieldk.nlp.jitar.training.TrainingHandler;
import eu.danieldk.nlp.jitar.wordhandler.KnownWordHandler;
import eu.danieldk.nlp.jitar.wordhandler.SuffixWordHandler;
import eu.danieldk.nlp.jitar.wordhandler.WordHandler;

public class Tagger {

	private static final String START = "<START>";
	private static final String END = "<END>";

	private final Model model;
	private final HMMTagger tagger;
	private final Lexicon lexicon;

	public Tagger(Treebank treebank, Lexicon lexicon) {
		this(treebank.commands(), lexicon);
	}

	public Tagger(Iterable<Command> commands) {
		this(commands, null);
	}

	private Tagger(Iterable<Command> commands, Lexicon lexicon) {

		// Lexicon.
		this.lexicon = lexicon;

		// Train.
		Log.info("Training tagger...");
		TrainingHandler trainingHandler = new TrainingHandler();
		for (Command command : commands) {
			if (command.losr() == null) {
				continue;
			}
			List<TaggedWord> sentence = new ArrayList<TaggedWord>();
			sentence.add(new TaggedWord(START, START));
			sentence.add(new TaggedWord(START, START));
			Sequence sequence = new Sequence(command);
			int size = sequence.count();
			for (int i = 0; i < size; i++) {
				sentence.add(new TaggedWord(sequence.text(i), sequence.tag(i)));
			}
			sentence.add(new TaggedWord(END, END));
			trainingHandler.handleSentence(sentence);
		}

		// Unigrams.
		Map<String, Integer> tagNumbers = new HashMap<>();
		Map<Integer, String> numberTags = new HashMap<>();
		Map<UniGram, Integer> unigrams = new HashMap<>();
		int tagNumber = 0;
		for (Entry<String, Integer> entry : trainingHandler.uniGrams()
				.entrySet()) {
			int frequency = entry.getValue();
			tagNumbers.put(entry.getKey(), tagNumber);
			numberTags.put(tagNumber, entry.getKey());
			unigrams.put(new UniGram(tagNumber), frequency);
			tagNumber++;
		}

		// Bigrams.
		Map<BiGram, Integer> bigrams = bigrams(trainingHandler, tagNumbers);

		// Trigrams.
		Map<TriGram, Integer> trigrams = trigrams(trainingHandler, tagNumbers);

		// Word tag frequencies.
		Map<String, Map<Integer, Integer>> wordTagFrequencies = wordTagFrequencies(
				trainingHandler, tagNumbers);

		// Model.
		model = new Model(wordTagFrequencies, tagNumbers, numberTags, unigrams,
				bigrams, trigrams);

		// Tagger.
		SuffixWordHandler suffixWordHandler = new SuffixWordHandler(
				model.lexicon(), model.uniGrams(), 2, 2, 8, 10, 10);
		WordHandler wordHandler = new KnownWordHandler(model.lexicon(),
				model.uniGrams(), suffixWordHandler);
		LanguageModel languageModel = new LinearInterpolationLM(
				model.uniGrams(), model.biGrams(), model.triGrams());
		tagger = new HMMTagger(model, wordHandler, languageModel, 1000);
	}

	public Items<Terminal> terminals(Command command) {
		return sequence(command.tokens()).terminals(lexicon);
	}

	public Sequence sequence(String text) {
		return sequence(new Tokenizer(text).tokens());
	}

	public Sequence sequence(Items<Terminal> terminals) {

		// Words.
		List<String> words = new ArrayList<String>();
		words.add(START);
		words.add(START);
		for (Terminal terminal : terminals) {
			words.add(terminal.context().text());
		}
		words.add(END);

		// Tags.
		List<String> hmmTags = HMMTagger.highestProbabilitySequence(
				tagger.viterbi(words), model).sequence();

		// Sequence.
		return new Sequence(terminals, hmmTags);
	}

	private static Map<BiGram, Integer> bigrams(
			TrainingHandler trainingHandler, Map<String, Integer> tagNumbers) {

		Map<BiGram, Integer> bigrams = new HashMap<>();
		for (Entry<String, Integer> entry : trainingHandler.biGrams()
				.entrySet()) {
			int frequency = entry.getValue();
			String[] parts = entry.getKey().split(" ");
			bigrams.put(
					new BiGram(tagNumbers.get(parts[0]), tagNumbers
							.get(parts[1])), frequency);
		}
		return bigrams;
	}

	private static Map<TriGram, Integer> trigrams(
			TrainingHandler trainingHandler, Map<String, Integer> tagNumbers) {

		Map<TriGram, Integer> trigrams = new HashMap<>();
		for (Entry<String, Integer> entry : trainingHandler.triGrams()
				.entrySet()) {
			int frequency = entry.getValue();
			String[] parts = entry.getKey().split(" ");
			trigrams.put(
					new TriGram(tagNumbers.get(parts[0]), tagNumbers
							.get(parts[1]), tagNumbers.get(parts[2])),
					frequency);
		}
		return trigrams;
	}

	private Map<String, Map<Integer, Integer>> wordTagFrequencies(
			TrainingHandler trainingHandler, Map<String, Integer> tagNumbers) {

		Map<String, Map<Integer, Integer>> wordTagFrequencies = new HashMap<>();
		Map<String, Map<String, Integer>> lexicon = trainingHandler.lexicon();
		for (Entry<String, Map<String, Integer>> wordEntry : lexicon.entrySet()) {
			String word = wordEntry.getKey();
			wordTagFrequencies.put(word, new HashMap<>());
			for (Entry<String, Integer> tagEntry : lexicon.get(word).entrySet()) {
				wordTagFrequencies.get(word).put(
						tagNumbers.get(tagEntry.getKey()), tagEntry.getValue());
			}
		}
		return wordTagFrequencies;
	}
}