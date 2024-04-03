package server.message;

import controller.GameController.PieceMoveForStash;

public record GameStash(int lastRoll, PieceMoveForStash pieceMoved) {
}
