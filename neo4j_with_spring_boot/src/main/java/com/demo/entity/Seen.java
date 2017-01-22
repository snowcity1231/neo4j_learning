package com.demo.entity;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import lombok.Data;

@Data
@RelationshipEntity(type = "HAS_SEEN")
public class Seen {
	
	public Seen(Integer stars, User startNode, Movie endNode) {
		this.stars = stars;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	@GraphId
	private Long id;
	@Property
	private Integer stars;
	@StartNode
	private User startNode;
	@EndNode
	private Movie endNode;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the stars
	 */
	public Integer getStars() {
		return stars;
	}
	/**
	 * @param stars the stars to set
	 */
	public void setStars(Integer stars) {
		this.stars = stars;
	}
	/**
	 * @return the startNode
	 */
	public User getStartNode() {
		return startNode;
	}
	/**
	 * @param startNode the startNode to set
	 */
	public void setStartNode(User startNode) {
		this.startNode = startNode;
	}
	/**
	 * @return the endNode
	 */
	public Movie getEndNode() {
		return endNode;
	}
	/**
	 * @param endNode the endNode to set
	 */
	public void setEndNode(Movie endNode) {
		this.endNode = endNode;
	}
}
