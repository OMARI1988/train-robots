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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.nlp.tokenizer.Tokenizer;

import eu.danieldk.nlp.jitar.corpus.TaggedWord;
import eu.danieldk.nlp.jitar.data.Model;
import eu.danieldk.nlp.jitar.languagemodel.LanguageModel;
import eu.danieldk.nlp.jitar.languagemodel.LinearInterpolationLM;
import eu.danieldk.nlp.jitar.tagger.HMMTagger;
import eu.danieldk.nlp.jitar.training.TrainingHandler;
import eu.danieldk.nlp.jitar.wordhandler.KnownWordHandler;
import eu.danieldk.nlp.jitar.wordhandler.SuffixWordHandler;
import eu.danieldk.nlp.jitar.wordhandler.WordHandler;

public class JitarChunker implements Chunker {

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

		try {
			writeLexicon(trainHandler.lexicon(), new BufferedWriter(
					new FileWriter("jitar-data/lexicon.dat")));
			writeNGrams(trainHandler.uniGrams(), trainHandler.biGrams(),
					trainHandler.triGrams(), new BufferedWriter(new FileWriter(
							"jitar-data/ngrams.dat")));

			model = Model.readModel(new BufferedReader(new FileReader(
					"jitar-data/lexicon.dat")), new BufferedReader(
					new FileReader("jitar-data/ngrams.dat")));

			SuffixWordHandler swh = new SuffixWordHandler(model.lexicon(),
					model.uniGrams(), 2, 2, 8, 10, 10);
			WordHandler wh = new KnownWordHandler(model.lexicon(),
					model.uniGrams(), swh);
			LanguageModel lm = new LinearInterpolationLM(model.uniGrams(),
					model.biGrams(), model.triGrams());

			tagger = new HMMTagger(model, wh, lm, 1000.0);

		} catch (IOException exception) {
			throw new CoreException(exception);
		}
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

	private static void writeNGrams(Map<String, Integer> uniGrams,
			Map<String, Integer> biGrams, Map<String, Integer> triGrams,
			BufferedWriter writer) throws IOException {
		for (Entry<String, Integer> entry : uniGrams.entrySet())
			writer.write(entry.getKey() + " " + entry.getValue() + "\n");

		for (Entry<String, Integer> entry : biGrams.entrySet())
			writer.write(entry.getKey() + " " + entry.getValue() + "\n");

		for (Entry<String, Integer> entry : triGrams.entrySet())
			writer.write(entry.getKey() + " " + entry.getValue() + "\n");

		writer.flush();
	}

	private static void writeLexicon(Map<String, Map<String, Integer>> lexicon,
			BufferedWriter writer) throws IOException {
		for (Entry<String, Map<String, Integer>> wordEntry : lexicon.entrySet()) {
			String word = wordEntry.getKey();

			writer.write(word);

			for (Entry<String, Integer> tagEntry : lexicon.get(word).entrySet()) {
				writer.write(" ");
				writer.write(tagEntry.getKey());
				writer.write(" ");
				writer.write(tagEntry.getValue().toString());
			}

			writer.newLine();
		}

		writer.flush();
	}
}