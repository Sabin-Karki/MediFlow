# okay so the plan for the whole patient process:
first create patient dto -> patient_id,first_name,second_name,dob
this dto is to be worked upon service layer without touching up entity as it is the final layer /the data in db which ohould probably not be operated,the whole reason dto is used is to get the exact thing that comes from file and operate on it
now use controller and create a method to handle upload request
use service layer and recieve patient dto and do normalization and save it to database//during service layer use rawdataevent for asynchronous processing

@Async
public void processPipeLine(Long id) {
//monitor the database of raw data event and check the status,when it sees the status it changes received to processing,it then gets the file path and processes it,determins the type of file it is and then process it accordingly //after processing change the status code to completed//i want to use async processing not scheduled !!!
Optional<RawDataEvent> optionalEvent = rawDataEventRepository.findById(id);
if (optionalEvent.isPresent()) {
RawDataEvent rawDataEvent = optionalEvent.get();
//update status to processing after retriving the object
try {
rawDataEvent.setStatus(JobStatus.PROCESSING);
rawDataEventRepository.save(rawDataEvent);
## so till this process now what is done is,rawDataEvent now has the object of the table of RawDataaEvent when the status was recieved now it is saved in rawdataevent then a tryblock is executed to update status to processing and then it is saved<the new object > is saved too RawDataEvent using .save(),
## now that i have saved it,i do this thing where i say check if the file type is csv and if it is,then call a method called say patientProcess("idk what the parameter shall me,like maybe MultiPartFile file or id,,which will check the RawDAtaEvent status and if its processing the parsing logic and then comapring to DataSeeder class and transforming the dto and then saving to entity of PAtient finally);
} catch (Exception e) {
System.err.println("Failed to update status" + e.getMessage());
