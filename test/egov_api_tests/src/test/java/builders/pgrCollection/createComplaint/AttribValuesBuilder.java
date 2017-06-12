package builders.pgrCollection.createComplaint;

import entities.requests.pgrCollections.createComplaint.AttribValues;

public class AttribValuesBuilder {

    AttribValues[] attribValues = new AttribValues[4];

    AttribValues attribValues1 = new AttribValues();
    AttribValues attribValues2 = new AttribValues();
    AttribValues attribValues3 = new AttribValues();
    AttribValues attribValues4 = new AttribValues();

    public AttribValuesBuilder(){
        attribValues1.setKey("receivingMode");
        attribValues1.setName("MANUAL");
        attribValues[0] = attribValues1;
        attribValues2.setKey("receivingCenter");
        attribValues2.setName("5");
        attribValues[1] = attribValues2;
        attribValues3.setKey("status");
        attribValues3.setName("REGISTERED");
        attribValues[2] = attribValues3;
        attribValues4.setKey("complainantAddress");
        attribValues4.setName("erode");
        attribValues[3] = attribValues4;
    }

    public AttribValuesBuilder(String status)
    {
        attribValues1.setKey("receivingMode");
        attribValues1.setName("MANUAL");
        attribValues[0] = attribValues1;
        attribValues2.setKey("receivingCenter");
        attribValues2.setName("5");
        attribValues[1] = attribValues2;
        attribValues3.setKey("status");
        attribValues3.setName(status);
        attribValues[2] = attribValues3;
        attribValues4.setKey("complainantAddress");
        attribValues4.setName("erode");
        attribValues[3] = attribValues4;
    }
    public AttribValues[] build(){
        return attribValues;
    }
}
