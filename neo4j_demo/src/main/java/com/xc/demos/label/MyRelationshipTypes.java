package com.xc.demos.label;

import org.neo4j.graphdb.RelationshipType;

/** 
* @ClassName: MyRelationshipTypes 
* @Description: TODO
* @author xuechen
* @date 2017年1月22日 下午3:58:15
*  
*/
public enum MyRelationshipTypes implements RelationshipType{
	IS_FRIEND_OF,HAS_SEEN
}
