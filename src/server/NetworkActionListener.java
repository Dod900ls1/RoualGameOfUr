package server;

import controller.PlayerRemoteController;

import java.awt.event.ActionListener;

public interface NetworkActionListener extends ActionListener {
    public void setPlayerRemoteController(PlayerRemoteController playerRemoteController);

    void receiveMessageFromRemote(Message message);
}
