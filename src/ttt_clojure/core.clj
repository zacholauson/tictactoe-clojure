(ns ttt-clojure.core
  (:require [ttt-clojure.gamestate          :refer [create-board]]
            [ttt-clojure.tic-tac-toe        :refer [ttt]]
            [ttt-clojure.interface.player   :refer :all]
            [ttt-clojure.players.computer   :refer [new-computer]]
            [ttt-clojure.players.human      :refer [new-human]]
            [ttt-clojure.interface.display  :refer :all]
            [ttt-clojure.displays.console   :refer [new-display-console]]
            [ttt-clojure.interface.prompter :refer :all]
            [ttt-clojure.prompters.console  :refer [new-console-prompter]]))

(defmulti build-gamestate (fn [display prompter row-size gametype] gametype))

(defmethod build-gamestate :computer-vs-human [display prompter row-size gametype]
  (let [players [(new-computer :x) (new-human :o prompter)]
        difficulty-setting (ask prompter "What difficulty would you like? " [:unbeatable :medium :easy])
        computer-mark :x]
    {:board (create-board row-size) :players players :computer computer-mark :options {:difficulty difficulty-setting}}))

(defmethod build-gamestate :human-vs-computer [display prompter row-size gametype]
  (let [players [(new-human :x prompter) (new-computer :o)]
        difficulty-setting (ask prompter "What difficulty would you like? " [:unbeatable :medium :easy])
        computer-mark :o]
    {:board (create-board row-size) :players players :computer computer-mark :options {:difficulty difficulty-setting}}))

(defmethod build-gamestate :computer-vs-computer [display prompter row-size gametype]
  (let [players [(new-computer :x) (new-computer :o)]
        difficulty-setting (ask prompter "What difficulty would you like? " [:unbeatable :medium :easy])
        computer-mark :x]
    {:board (create-board row-size) :players players :computer computer-mark :options {:difficulty difficulty-setting}}))

(defmethod build-gamestate :human-vs-human [display prompter row-size gametype]
  (let [players [(new-human :x prompter) (new-human :o prompter)]]
    {:board (create-board row-size) :players players :options {:difficulty :easy}}))

(defn build-gamestate-and-start-game []
  (let [display            (new-display-console)
        prompter           (new-console-prompter display)
        gametype           (ask prompter "What gamemode would you like to play?" [:computer-vs-human :human-vs-computer :computer-vs-computer :human-vs-human])
        row-size           (ask prompter "What board ( row ) size would you like?" [3 4 5])
        gamestate          (build-gamestate display prompter row-size gametype)]
       (ttt gamestate display prompter)))

(defn -main []
  (build-gamestate-and-start-game))
