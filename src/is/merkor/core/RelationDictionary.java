/*******************************************************************************
 * MerkOrCore
 * Copyright (c) 2012 Anna B. Nikulásdóttir
 * 
 * License: GNU Lesser General Public License. 
 * See: <http://www.gnu.org/licenses> and <README.markdown>
 * 
 *******************************************************************************/
package is.merkor.core;

import java.util.List;

/**
 * An interface for the access of {@link Relation}s.
 * 
 * @author Anna B. Nikulasdottir
 * @version 0.8
 */
public interface RelationDictionary {
	
	/**
	 * Retrieves all {@link Relation}s for {@code lemma}.
	 * They are wrapped in a {@link RelationObject} since {@code lemma} possibly has more
	 * than one sense. A RelationObject is created for each sense of the lemma with the
	 * associated relations.
	 * 
	 * @param lemma the lemma for which to retrieve relations, may not be {@code null} or emtpy.
	 * @return a List of RelationObjects containing LexicalRelations for lemma, or an empty
	 * 			List if nothing is found.
	 * @throws IllegalArgumentException if param {@code null} or empty.
	 */
	public List<RelationObject> getRelationsFor (String lemma);
	
	/**
	 * Retrieves all {@link Relation}s for {@code item}.
	 * 
	 * @param item the item for which to retrieve relations, may not be {@code null} or emtpy.
	 * @return a sorted List of RelationObjects containing LexicalRelations for lemma, or an empty
	 * 			List if nothing is found.
	 * @throws IllegalArgumentException if param {@code null} or empty.
	 */
	public List<? extends Relation> getRelationsFor (Item item);
	
	/**
	 * Retrieves the {@link Relation} connecting {@code lemma1} and
	 * {@code lemma2}. In some cases different senses of the two lemmata might be related, hence
	 * the return of a List of relations, even if it will in most cases only contain one element
	 * (or none if no relation is found). 
	 * 
	 * @param lemma1 a lemma to search relation for in combination with lemma2, may not be {@code null}
	 * 			or empty.
	 * @param lemma2 a lemma to search relation for in combination with lemma1, may not be {@code null}
	 * 			or empty.
	 * @return a List of RelationObjects connecting lemma1 and lemma2, or an empty List if nothing is found.
	 * @throws IllegalArgumentException if params are {@code null} or empty
	 */
	public List<RelationObject> getRelationsFor (String lemma1, String lemma2);
	
	/**
	 * Retrieves the {@link Relation} connecting {@code item1} and
	 * {@code item2}. 
	 * 
	 * @param item1 an item to search relation for in combination with item2, may not be {@code null}
	 * @param item2 an item to search relation for in combination with item1, may not be {@code null}
	 * @return a sorted List of LexicalRelations connecting item1 and item2, or an empty List if nothing is found.
	 * @throws IllegalArgumentException if params are {@code null}
	 */
	public List<Relation> getRelationsFor (final Item item1, final Item item2);
	
	/**
	 * Retrieves all {@link Relation}s having {@code lemma} as the left item and {@code type} as
	 * {@link RelationType}.
	 * 
	 * @param lemma the left lemma of the Relations to retrieve
	 * @param type the RelationType of the Relations to retrieve
	 * @return a List of RelationObjects matching the parameter criteria
	 * @throws IllegalArgumentException if params are {@code null} or empty
	 */
	public List<RelationObject> getRelationsHavingLeft (String lemma, RelationType type);
	
	/**
	 * Retrieves all {@link Relation}s having {@code item} as the left item and {@code type} as
	 * {@link RelationType}.
	 * 
	 * @param item the left item of the Relations to retrieve
	 * @param type the RelationType of the Relations to retrieve
	 * @return a sorted List of Relations matching the parameter criteria
	 * @throws IllegalArgumentException if params are {@code null}
	 */
	public List<Relation> getRelationsHavingLeft (Item item, RelationType type);
	
	/**
	 * Retrieves all {@link Relation}s having {@code lemma} as the right item and {@code type} as
	 * {@link RelationType}.
	 * 
	 * @param lemma the right lemma of the Relations to retrieve
	 * @param type the RelationType of the Relations to retrieve
	 * @return a List of RelationObjects matching the parameter criteria
	 * @throws IllegalArgumentException if params are {@code null} or empty
	 */
	public List<RelationObject> getRelationsHavingRight (String lemma, RelationType type);
	
	/**
	 * Retrieves all {@link Relation}s having {@code item} as the right item and {@code type} as
	 * {@link RelationType}.
	 * 
	 * @param item the right item of the Relations to retrieve
	 * @param type the RelationType of the Relations to retrieve
	 * @return a sorted List of Relations matching the parameter criteria
	 * @throws IllegalArgumentException if params are {@code null}
	 */
	public List<Relation> getRelationsHavingRight (Item item, RelationType type);
	 
}
