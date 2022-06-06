package dhbw.mwi.Auslandsemesterportal2016.camunda;

public class GoOutConnector {

    public void process() {
        sendDataToGoOutForm(getRelevantProcessData());
    }

    private BewerbungsDaten getRelevantProcessData() {
        return null;
    }

    private void sendDataToGoOutForm(BewerbungsDaten bewerbungsDaten) {

    }
}
