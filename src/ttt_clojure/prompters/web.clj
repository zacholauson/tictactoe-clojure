(ns ttt-clojure.prompters.web
  (:require [ttt-clojure.interface.prompter :refer :all]
            [ttt-clojure.interface.display  :refer :all]
            [clojure.data.json              :as    json]))

(deftype Web [-display]
  Prompter
  (ask-for-move [this gamestate]
    "Next Move")
  (ask-for-first-player [this]
    "First-player")
  (ask-for-difficulty [this]
    "Difficulty")
  (ask-for-board-size [this]
    "Board-size"))

(defn new-web-prompter [display]
  (Web. display))
