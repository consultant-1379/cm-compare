if (System.properties.'skip.tests' || System.properties.'skip.fit.tests')
	return

String targetDir = project.build.directory
String currentProject = project.artifactId
boolean windows = System.properties.'os.name'.toString().toLowerCase().contains("windows")
String cpSeparator = (windows)?";" : ":"
println "Looking for test case jar in ${targetDir}"
String CP = new File(targetDir).listFiles().find {it.name.endsWith(".jar")}.absolutePath
println "Test case jar found $CP"
println "Setting classpath"
println "Running windows = $windows"
if (windows) {
	CP += cpSeparator + new File("$targetDir\\lib").absolutePath+"\\*"
} else{
	//	CP += cpSeparator + new File("$targetDir/lib").listFiles().collect { it.absolutePath }.join(cpSeparator)
	CP += cpSeparator + "$targetDir/lib/*"
}
println "Classpath = $CP"
CP=CP.replace("\\","/")
String suite=""
if (! System.properties.'suites' || System.properties.'suites'.size() < 1){
	suite = "$targetDir/suites/listeners.xml " + new File("$targetDir/suites/").listFiles().collect { it.absolutePath }.findAll {!it.endsWith("/listeners.xml")}.findAll {it.endsWith(".xml")}.sort().join(" ")
}
else {
	suite =  "$targetDir/suites/listeners.xml " + System.properties.'suites'.split(" ").collect { "$targetDir/suites/$it" }.join(" ")
}
String groups = ""
if (System.properties.'groups'?.size() > 0)
	groups = "-groups ${System.properties.'groups'}"

String tafListeners = ""
if (System.properties.'tafListeners'?.size() > 0)
	tafListeners = "-listener ${System.properties.'tafListeners'}"

String logdir =  "$targetDir/Jcat_LOGS"
String name = "TAFFIT"
String jarfile=""
String props=""
String els=""
String transparentProperties=""

System.properties.findAll { ! it.key.contains("line.separator")}.each {
	if (it.value.contains(" "))
		transparentProperties+="\"\"-D$it.key=\"$it.value\"\"\" "
	else transparentProperties+="-D$it.key=$it.value "
}
String logdb="$targetDir/resources/logdb.properties"
String logwriters=""

String vmargs = " -Xms1024M -Xmx1024M -XX:MaxPermSize=256M -DprojectName=$currentProject -DfetchLogs=true -Dname=$name -Dlogdir=$logdir -Dlegacylogging=\"\" ${(props.size() > 0)? '-Dprops=$props' : ''} -Dlogwriters=$logwriters ${(els.size() > 0)? '-Dels=$els' : ''}"
println "Preparing run command"
new File("run.bat").with {
	it.delete()
	String runLine = "java $vmargs ${transparentProperties}-cp $CP com.ericsson.cifwk.taf.Taf $suite $groups $tafListeners"
	it << runLine
	println "Will run using $runLine"
	it.setExecutable(true)
	if (! System.properties.'keep.run.bat')
		it.deleteOnExit()

	println "Command file prepared $it.absolutePath"
}
String runCommand = (windows)? "cmd /c run.bat" : "./run.bat"
println "Starting test run"
def proc = runCommand.execute()

proc.consumeProcessOutput(System.out, System.err)
proc.waitFor()
return proc.exitValue()

//comment
