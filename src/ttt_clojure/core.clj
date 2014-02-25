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

(defn build-gamestate-and-start-game []
  (let [display            (new-display-console)
        prompter           (new-console-prompter display)
        who-goes-first     (ask-for-first-player prompter)
        difficulty-setting (ask-for-difficulty prompter)
        row-size           (ask-for-board-size prompter)
        computer           (new-computer (if (= who-goes-first :computer) :x :o))
        human              (new-human    (if (= who-goes-first :computer) :o :x) prompter)
        player-col         (if (= who-goes-first :computer) [computer human] [human computer])
        computer-mark      (if (= who-goes-first :computer) :x :o)]
       (ttt {:board (create-board row-size) :players player-col :computer computer-mark :options {:difficulty difficulty-setting}} display prompter)))

(defn -main []
  (build-gamestate-and-start-game))
