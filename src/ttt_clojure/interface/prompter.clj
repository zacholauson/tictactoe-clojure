(ns ttt-clojure.interface.prompter)

(defprotocol Prompter
  (ask-for-move         [this gamestate])
  (ask-for-first-player [this])
  (ask-for-difficulty   [this])
  (ask-for-board-size   [this]))
