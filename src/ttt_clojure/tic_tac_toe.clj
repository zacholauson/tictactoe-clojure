(ns ttt-clojure.tic-tac-toe
  (:require [ttt-clojure.gamestate          :refer [move game-over? winner]]
            [ttt-clojure.interface.player   :refer :all]
            [ttt-clojure.players.computer   :refer [new-computer]]
            [ttt-clojure.players.human      :refer [new-human]]
            [ttt-clojure.interface.display  :refer :all]
            [ttt-clojure.displays.console   :refer [new-display-console]]
            [ttt-clojure.interface.prompter :refer :all]
            [ttt-clojure.prompters.console  :refer [new-console-prompter]]))

(defn get-next-move [gamestate]
  (next-move (first (:players gamestate)) gamestate))

(defn ttt [gamestate display prompter]
  (print-board display (:board gamestate))
  (if (game-over? gamestate) (declare-winner display (winner gamestate))
      (ttt (move gamestate (get-next-move gamestate)) display prompter)))
