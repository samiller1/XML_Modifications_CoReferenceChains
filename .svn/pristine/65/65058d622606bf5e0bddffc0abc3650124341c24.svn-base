
@Grab("org.anc:common:3.1.0")
import org.anc.io.*

class XmlModifier {
	
	private Node node;
	private ArrayList<String> missingIDs;
	private HashMap<String, HashMap<String, String>> nodeData;
	private String file;
	private String textDirectory;
	
	
	/**
	 * Constructor for class XmlModifier
	 * @param node
	 */
	public XmlModifier(Node node, String file, String textDirectory){
		this.node = node;
		this.nodeData = new HashMap<String, HashMap<String, String>>()
		this.missingIDs = new ArrayList<String>()
		this.file = file;
		this.textDirectory = textDirectory;
	}
	
	/**
	 * This function initializes a hashmap to store the data of each node, referenced by its id number.
	 */
	private void initializeHashMap(){
		List<Node> nodeList = this.node.breadthFirst()
		Node firstNode = nodeList.get(0)
		List<Node> wordNodeList = firstNode.children()
		
		
		//Iterate through wordNodes
		for (int i = 0; i < wordNodeList.size(); i++){
			
			//Initialize ID field and a hashMap, featureMap
			String wordID = 'ID_Not_Found'
			HashMap<String, String> featureMap = new HashMap<String,String>()
			
			
			featureMap.put('classification', wordNodeList.get(i).attributes().get('type'))
			featureMap.put('from', wordNodeList.get(i).attributes().get('from'))
			featureMap.put('to', wordNodeList.get(i).attributes().get('to'))
			
			//Isolate the word node's children -- it's features
			List<Node> features = wordNodeList.get(i).children()
	
			
			for (int k = 0; k < features.size(); k++){
				if (features.get(k).attributes().get('name') == 'id'){
					wordID = features.get(k).attributes().get('value')
				}
				else{
					featureMap.put(features.get(k).attributes().get('name'), features.get(k).attributes().get('value'))
				}
			}
			
			
			this.nodeData.put(wordID, featureMap)
		}
	}
	
	
	
	
	/**
	 * This function iterates through the hashmap of the node data and adds the missing 'matches' features when necessary.
	 */
	private void addMissingMatchesFeatures(){
		//Copy word IDs into an array list
		ArrayList<String> IDs = this.nodeData.keySet()
		
		//Iterate through IDs, locate ones that have a 'matches' feature
		for (String id : IDs){
			
			if(this.nodeData.get(id).containsKey('matches') && (this.nodeData.get(id).get('matches').size() != 2)){
				
				String matchesNoClosures = this.nodeData.get(id).get('matches').substring(1, this.nodeData.get(id).get('matches').size() - 1)
				Set<String> matchSet = new HashSet<String>()
				matchSet.addAll(new ArrayList<String>(Arrays.asList(matchesNoClosures.split(","))))
				
				for (String match : matchSet){
					
					if(this.nodeData.containsKey(match)){
						
						//if the match already has a matches feature -- merge the two lists, eliminate repeats and replace both lists
						if(this.nodeData.get(match).containsKey('matches') && (this.nodeData.get(match).get('matches').size() != 2)){
							
							//Initialize a hashset -- ensures no repeat entries
							Set<String> newMatchList = new HashSet<String>()
							newMatchList.addAll(new ArrayList<String>(Arrays.asList(this.nodeData.get(match).get('matches').substring(1, this.nodeData.get(match).get('matches').size() - 1).split(","))))
							
							
							//Combine the entries of matchArray and newMatchList, then replace the 'matches' entry for match
							newMatchList.addAll(matchSet)
							newMatchList.remove(match)
							newMatchList.add(id)
							
							
							this.nodeData.get(match).remove('matches')
							this.nodeData.get(match).put('newMatches', newMatchList.toString())
							
							//Replace the original node's matches
							newMatchList.add(match)
							newMatchList.remove(id)

							
							this.nodeData.get(id).remove('matches')
							this.nodeData.get(id).put('newMatches', newMatchList.toString())
						}
						
						//the match does not already have a matches feature
						else{
							Set<String> newMatchList2 = new HashSet<String>()
							newMatchList2.addAll(matchSet)
							newMatchList2.remove(match)
							newMatchList2.add(id)
							this.nodeData.get(match).remove('matches')
							this.nodeData.get(match).put('newMatches', newMatchList2.toString())
						
						}
					}
					
					else{
						this.missingIDs.add(match)
					}
				}
			}
		}
		//System.out.println(this.nodeData.toMapString())
	}
		
	/**
	 * Print information to console about each coreference chain
	 */
	private void printChains(){
		System.out.println("-----COREFERENCE CHAIN SUMMARY-----")
		System.out.println("-----------------------------------")
		System.out.println('')
		ArrayList<String> IDs = this.nodeData.keySet()
		ArrayList<String> printedIDs = new ArrayList<String>()

		for (String id: IDs){
			
			if (this.nodeData.get(id).containsKey('newMatches') && (this.nodeData.get(id).get('newMatches').size() != 2) && (!(printedIDs.contains(id)))){
				
				printedIDs.add(id)
				System.out.println('Coreference chain starting at ID ' + id + ',' + this.nodeData.get(id).get('classification') + ': ' + this.getText(this.nodeData.get(id).get('from'), this.nodeData.get(id).get('to')))
				
				String matchesNoClosures = this.nodeData.get(id).get('newMatches').substring(1, this.nodeData.get(id).get('newMatches').size() - 1)
				ArrayList<String> matchSet = new ArrayList<String>(Arrays.asList(matchesNoClosures.split(", ")))
				
				int index = 0
				
				for(int i = 0; i < matchSet.size(); i ++){
					String matchString
					if(matchSet.get(i).charAt(0) == ' '){
						matchString = matchSet.get(i).substring(1, matchSet.get(i).size())
					}
					else{
						matchString = matchSet.get(i)
					}
					printedIDs.add(matchSet.get(i))
					if (this.missingIDs.contains(matchString)){
						System.out.println('  >>> ID ' + matchString + " DOES NOT EXIST")
					}
					else{
						index++
						System.out.println("  (" + index + ') ' + this.nodeData.get(matchString).get('classification') + ' ' + matchString + ' ' + this.nodeData.get(matchString).get('from') + ' ' + this.nodeData.get(matchString).get('to') + ' ' + this.getText(this.nodeData.get(matchString).get('from'), this.nodeData.get(matchString).get('to')))	
					}
					}
					
			}		
		}
	}
	
	
	/**
	 * Convert the nodeData hashMap back into nodes -- these will be written back to xml file 
	 */
	private Node hashMaptoNode(){
		//Initialize an arraylist to store all of the children nodes - then use this to clear the children from the first node
		List<Node> nodeList = this.node.breadthFirst()
		Node firstNode = nodeList.get(0)
		List<Node> wordNodeList = firstNode.children()
		ArrayList<Node> wordNodeListCopy = new ArrayList<Node>()
		wordNodeListCopy.addAll(wordNodeList)
		
		for (Node wordNode: wordNodeListCopy){
			firstNode.remove(wordNode)
		}
		
		//Iterate through the list of IDs and initialize nodes for each word
		for (String id : this.nodeData.keySet()){
			Map<String, String> attributeMap = new HashMap<String, String>()
			attributeMap.put('to', this.nodeData.get(id).get('to'))
			attributeMap.put('from', this.nodeData.get(id).get('from'))
			attributeMap.put('type', this.nodeData.get(id).get('classification'))
		
			
			Node newNode = new Node(firstNode, 'struct', attributeMap)
			Map<String, String> idMap = new HashMap<String, String>()
			idMap.put('name', 'id')
			idMap.put('value', id)
			Node idNode = new Node(newNode, 'feat', idMap)
			
			for (String feature: this.nodeData.get(id).keySet()){
				Map<String, String> featureAttributeMap = new HashMap<String, String>()
				
				if((feature != 'to') && (feature != 'from') && (feature != 'classification')){
				
					if (feature != 'newMatches'){
						featureAttributeMap.put('name', feature)
						featureAttributeMap.put('value', this.nodeData.get(id).get(feature))
						Node newFeatureNode = new Node(newNode, 'feat', featureAttributeMap)
					}
				
					else{
						featureAttributeMap.put('name', 'matches')
						featureAttributeMap.put('value', this.nodeData.get(id).get(feature))
						Node newMatchesNode = new Node(newNode, 'feat', featureAttributeMap)
					}
				}
				}		
		}
		return this.node
	}	


/**
 * Print information to console about each coreference chain
 */
private void printChainsToFile(String outputDirectory, String fileRoot){
	
	String[] fileRootSplit = fileRoot.split('-')
	String fileName = fileRootSplit[0]
	
	PrintWriter writer = new PrintWriter(new File(outputDirectory + '/' + fileName + '-coref-chains.txt')) 
	writer.println("-----COREFERENCE CHAIN SUMMARY-----")
	writer.println("-----------------------------------")
	writer.println('')
	
	ArrayList<String> IDs = this.nodeData.keySet()
	ArrayList<String> printedIDs = new ArrayList<String>()

	for (String id: IDs){
		
		if (this.nodeData.get(id).containsKey('newMatches') && (this.nodeData.get(id).get('newMatches').size() != 2) && (!(printedIDs.contains(id)))){
			
			printedIDs.add(id)
			writer.println('Coreference chain starting at ID ' + id + ',' + this.nodeData.get(id).get('classification') + ': ' + this.getText(this.nodeData.get(id).get('from'), this.nodeData.get(id).get('to')))
			
			String matchesNoClosures = this.nodeData.get(id).get('newMatches').substring(1, this.nodeData.get(id).get('newMatches').size() - 1)
			ArrayList<String> matchSet = new ArrayList<String>(Arrays.asList(matchesNoClosures.split(", ")))
			
			int index = 0
			
			for(int i = 0; i < matchSet.size(); i ++){
				String matchString
				if(matchSet.get(i).charAt(0) == ' '){
					matchString = matchSet.get(i).substring(1, matchSet.get(i).size())
				}
				else{
					matchString = matchSet.get(i)
				}
				printedIDs.add(matchSet.get(i))
				if (this.missingIDs.contains(matchString)){
					writer.println('  >>> ID ' + matchString + " DOES NOT EXIST")
				}
				else{
					index++
					writer.println("  (" + index + ') ' + this.nodeData.get(matchString).get('classification') + ' ' + matchString + ' ' + this.nodeData.get(matchString).get('from') + ' ' + this.nodeData.get(matchString).get('to') + ' ' + this.getText(this.nodeData.get(matchString).get('from'), this.nodeData.get(matchString).get('to')))
				}
				}
				
		}
	}
	writer.close()
}

/**
 * Retrieve the original text of an item using a UTF8Reader. 
 * @param from
 * @param to
 * @return
 */
private String getText(String from, String to){
	Integer fromInt = Integer.parseInt(from)
	Integer toInt = Integer.parseInt(to)
	UTF8Reader reader = new UTF8Reader(new File(this.textDirectory + '/' + this.file + '.txt'))
	String contents = reader.readString();
	reader.close();
	return contents.substring(fromInt, toInt);
}

}

