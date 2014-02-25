(ns ttt-clojure.core
  (:require [ttt-clojure.gamestate          :refer [move create-board game-over? winner]]
            [ttt-clojure.ai                 :refer [find-move]]
            [ttt-clojure.interface.player   :refer :all]
            [ttt-clojure.interface.display  :refer :all]
            [ttt-clojure.interface.prompter :refer :all]
            [ttt-clojure.players.computer   :refer [new-computer]]
            [ttt-clojure.players.human      :refer [new-human]]
            [ttt-clojure.displays.console   :refer [new-display-console]]
            [ttt-clojure.prompters.console  :refer [new-console-prompter]]))

(defn get-next-move [gamestate]
  (next-move (first (:players gamestate)) gamestate))

(defn ttt [gamestate display prompter]
  (print-board display (:board gamestate))
  (if (game-over? gamestate) (declare-winner display (winner gamestate))
    (ttt (move gamestate (get-next-move gamestate)) display prompter)))

(defn -main []
  (let [display            (new-display-console)
        prompter           (new-console-prompter display)
        who-goes-first     (ask-for-first-player prompter)
        difficulty-setting (ask-for-difficulty prompter)
        row-size           (ask-for-board-size prompter)
        computer           (new-computer (if (= who-goes-first :computer) :x :o))
        human              (new-human    (if (= who-goes-first :computer) :o :x) prompter)]
       (ttt {:board        (create-board row-size)
             :players      (if (= who-goes-first :computer) [computer human] [human computer])
             :computer     (if (= who-goes-first :computer) :x :o)
             :options      {:difficulty difficulty-setting}} display prompter)))
