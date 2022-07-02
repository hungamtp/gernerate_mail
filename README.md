#Build using maven
mvn clean compile assembly:single
mvn install

#After build by maven the jar file is located in target folder 
cd target
#Run
java -cp sendEmail-1.0-SNAPSHOT.jar com.sendmail.Main <template_file_path> <customer_file_path> <outout_folder> <error_file_path>

ex :java -cp sendEmail-1.0-SNAPSHOT.jar com.sendmail.Main ../input/email_template.json ../input/customers.csv ../output ../errors/errors.csv
