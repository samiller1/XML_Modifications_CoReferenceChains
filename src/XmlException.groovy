import java.io.File;


class Main {
	
	public static void main(String[] args){
		
		
		//Read user input
		String inputDirectory = args[0]
		String outputDirectory = args[1]
		String textDirectory = args[2]
		
		File inputDirFile = new File(inputDirectory);
		String[] inputFiles = inputDirFile.list();
		
		for (String inputFile: inputFiles){
			String fileName = inputDirectory + "/" + inputFile;
				
		String[] fileString = fileName.split('/')
		
		String root = fileString[fileString.size()-1]

		String[] rootString = root.split('-')
		String name = rootString[0];
		
		if(rootString.length > 2){
			for (int i = 1; i < (rootString.length - 1); i++){
				name = name + "-" + rootString[i];
				}
			}
			
		
		//Parse xml file using XmlParser
		File file = new File(fileName)
		XmlParser parser = new XmlParser()
		Node tree = parser.parse(file)

		XmlModifier modifier = new XmlModifier(tree, name, textDirectory)
		modifier.initializeHashMap()
		modifier.addMissingMatchesFeatures()
		modifier.printChains()
		Node newTree = modifier.hashMaptoNode()

		XmlNodePrinter printer = new XmlNodePrinter(new PrintWriter(new FileWriter(outputDirectory + '/Modified_' + root)))
		printer.print(newTree)
		
		modifier.printChainsToFile(outputDirectory, root)
		}
	}	
	}


