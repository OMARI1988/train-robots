/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.nlp.chunker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.nlp.tokenizer.Tokenizer;

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

public class JitarChunker extends Chunker {

	private Model model;
	private HMMTagger tagger;

	public void train(List<Command> commands) {

		// Train.
		System.out.println("Training...");
		TrainingHandler trainHandler = new TrainingHandler();
		for (Command command : commands) {
			List<TaggedWord> sentence = new ArrayList<TaggedWord>();
			sentence.add(new TaggedWord("<START>", "<START>"));
			sentence.add(new TaggedWord("<START>", "<START>"));
			for (Token token : new GoldSequence(command).tokens()) {
				sentence.add(new TaggedWord(token.token, token.tag));
			}
			sentence.add(new TaggedWord("<END>", "<END>"));
			trainHandler.handleSentence(sentence);
		}

		Map<String, Integer> uniGrams = trainHandler.uniGrams();
		Map<String, Integer> biGrams = trainHandler.biGrams();
		Map<String, Integer> triGrams = trainHandler.triGrams();

		Map<String, Integer> tagNumbers = new HashMap<String, Integer>();
		Map<Integer, String> numberTags = new HashMap<Integer, String>();
		Map<UniGram, Integer> uniGramFreqs = new HashMap<UniGram, Integer>();
		Map<BiGram, Integer> biGramFreqs = new HashMap<BiGram, Integer>();
		Map<TriGram, Integer> triGramFreqs = new HashMap<TriGram, Integer>();

		int tagNumber = 0;
		for (Entry<String, Integer> entry : uniGrams.entrySet()) {
			int freq = entry.getValue();
			tagNumbers.put(entry.getKey(), tagNumber);
			numberTags.put(tagNumber, entry.getKey());
			uniGramFreqs.put(new UniGram(tagNumber), freq);
			++tagNumber;
		}
		for (Entry<String, Integer> entry : biGrams.entrySet()) {
			int freq = entry.getValue();
			String[] parts = entry.getKey().split(" ");
			biGramFreqs.put(
					new BiGram(tagNumbers.get(parts[0]), tagNumbers
							.get(parts[1])), freq);
		}
		for (Entry<String, Integer> entry : triGrams.entrySet()) {
			int freq = entry.getValue();
			String[] parts = entry.getKey().split(" ");
			triGramFreqs.put(
					new TriGram(tagNumbers.get(parts[0]), tagNumbers
							.get(parts[1]), tagNumbers.get(parts[2])), freq);
		}

		Map<String, Map<Integer, Integer>> wordTagFreqs = new HashMap<String, Map<Integer, Integer>>();
		Map<String, Map<String, Integer>> lexicon = trainHandler.lexicon();
		for (Entry<String, Map<String, Integer>> wordEntry : lexicon.entrySet()) {
			String word = wordEntry.getKey();

			wordTagFreqs.put(word, new HashMap<Integer, Integer>());

			for (Entry<String, Integer> tagEntry : lexicon.get(word).entrySet()) {
				wordTagFreqs.get(word).put(tagNumbers.get(tagEntry.getKey()),
						tagEntry.getValue());
			}
		}

		model = new Model(wordTagFreqs, tagNumbers, numberTags, uniGramFreqs,
				biGramFreqs, triGramFreqs);

		SuffixWordHandler swh = new SuffixWordHandler(model.lexicon(),
				model.uniGrams(), 2, 2, 8, 10, 10);
		WordHandler wh = new KnownWordHandler(model.lexicon(),
				model.uniGrams(), swh);
		LanguageModel lm = new LinearInterpolationLM(model.uniGrams(),
				model.biGrams(), model.triGrams());

		tagger = new HMMTagger(model, wh, lm, 1000.0);
	}

	public List<Token> getSequence(String text) {

		List<String> words = new ArrayList<String>();
		words.add("<START>");
		words.add("<START>");
		Node tokens = Tokenizer.getTokens(text);
		for (Node node : tokens.children) {
			words.add(node.getText());
		}
		words.add("<END>");

		List<String> tags = HMMTagger.highestProbabilitySequence(
				tagger.viterbi(words), model).sequence();

		List<Token> result = new ArrayList<Token>();
		for (int i = 0; i < tokens.children.size(); i++) {
			result.add(new Token(tokens.children.get(i).getText(), tags
					.get(i + 2)));
		}

		// Fix.
		for (int i = 1; i < result.size(); i++) {
			Token last = result.get(i - 1);
			Token token = result.get(i);
			if (token.tag.startsWith("I-") && !last.tag.equals(token.tag)) {
				String tag = token.tag.substring(2);
				if (!last.tag.equals("B-" + tag)) {
					token.tag = "B-" + tag;
				}
			}
		}

		return result;
	}
}