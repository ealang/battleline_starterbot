#!/bin/sh
PYTHON=/home/elang/github/boardgameaiengine/venv/bin/python
BATTLELINE=/home/elang/github/boardgameaiengine/Battleline.py

BOT1="java -jar target/scala-2.11/bot-assembly-1.2.jar"
BOT2=$BOT1

$PYTHON $BATTLELINE --player1-cmd "$BOT1 bot1" --player2-cmd "$BOT2 bot2"
