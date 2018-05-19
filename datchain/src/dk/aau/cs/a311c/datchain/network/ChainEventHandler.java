package dk.aau.cs.a311c.datchain.network;

public class ChainEventHandler {

    /*
    on startup:
        check if newer chain exists, if true, get and break;
        else broadcast own chain

    on added new block:
        check if newer chain exists, if true, get and handle error,
            asking validator to add again and note new information
        else broadcast newly updated chain
     */

}
