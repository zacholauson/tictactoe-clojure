(ns ttt-clojure.gamestate-spec
  (:require [speclj.core :refer           :all]
            [ttt-clojure.gamestate        :refer :all]
            [ttt-clojure.interface.player :refer :all]
            [ttt-clojure.players.human    :refer [new-human]]
            [ttt-clojure.players.computer :refer [new-computer]]))

(def parse-board
  (memoize
    (fn [board-string]
      (vec (map keyword (drop 1 (clojure.string/split board-string #"")))))))

(describe "#parse-board"
  (it "should take a board string and parse it into a gamestate-like board"
    (should= [:x :x :- :o :o :x :- :- :-] (parse-board "xx-oox---"))))



(describe "#first-move?"
  (let [gamestate {:board (parse-board "---------")}]
    (it "should return true if the game is brand new || no moves have been taken"
      (should= true (first-move? gamestate)))))

(describe "#count-of"
  (it "should return the count of the given piece in the board"
    (should= 2 (count-of {:board (parse-board "xxo------")} :x))
    (should= 1 (count-of {:board (parse-board "xxo------")} :o))))

(describe "#xs-turn?"
  (it "should evaluate the given gamestate and return true if its x's turn and false if not"
    (should= true  (xs-turn? {:board (parse-board "---------")}))
    (should= true  (xs-turn? {:board (parse-board "xoxo-----")}))
    (should= true  (xs-turn? {:board (parse-board "xoxoo----")}))
    (should= false (xs-turn? {:board (parse-board "xoxox----")}))))

(describe "#os-turn?"
  (it "should evaluate the given gamestate and return true if its o's turn and false if not"
    (should= false (os-turn? {:board (parse-board "---------")}))
    (should= true  (os-turn? {:board (parse-board "xoxox----")}))))

(describe "#turn"
  (it "should return :o if there is an greater number of :x's than :o's"
    (should= :o (turn {:board (parse-board "xoxox----")})))
  (it "should return :o if there is an lesser or equal number of :x's than :o's"
    (should= :x (turn {:board (parse-board "xoxo-----")}))))

(describe "#computers-turn?"
  (it "should return true if its the computers turn based on the gamestate"
    (should= true (computers-turn? {:board (parse-board "xoxo-----") :computer :x}))
    (should= true (computers-turn? {:board (parse-board "xoxox----") :computer :o}))))

(describe "#rows"
  (it "should return the row indexes for the given board-size"
    (should= [[0 1 2] [3 4 5] [6 7 8]] (rows 9))))

(describe "#columns"
  (it "should return the column indexes for the given board size"
    (should= [[0 3 6] [1 4 7] [2 5 8]] (columns 9))))

(describe "#step"
  (it "should take a step integer and a range to step through and return a vector of the numbers"
    (should= [0 2] (step 2 (range 3)))))

(describe "#right-diag"
  (it "should return the left diag of the given board size"
    (should= [2 4 6]    (right-diag 9))
    (should= [3 6 9 12] (right-diag 16))))

(describe "#calculate-winning-positions"
  (it "should return the winning positions for board of the given gamestate"
    (should= [[0 1 2] [3 4 5] [6 7 8] [0 3 6] [1 4 7] [2 5 8] [0 4 8] [2 4 6]]
      (calculate-winning-positions 9))))

(describe "#winning-lines"
  (it "should return a vector of all winning lines with whatever piece is in each position"
    (should= [[:- :- :-] [:- :- :-] [:- :- :-] [:- :- :-] [:- :- :-] [:- :- :-] [:- :- :-] [:- :- :-]]
             (winning-lines {:board (parse-board "---------")}))

    (should= [[:x :x :o] [:o :o :-] [:- :- :-] [:x :o :-] [:x :o :-] [:o :- :-] [:x :o :-] [:o :o :-]]
             (winning-lines {:board (parse-board "xxooo----")}))))

(describe "#win?"
  (context "x won"
    (let [gamestate {:board (parse-board "xxx------")}]
      (it "should return true if 'x' has won"
        (should= true (win? gamestate :x))))))
  (context "o won"
    (let [gamestate {:board (parse-board "-o-xo-xo-")}]
      (it "should return true if 'o' has won"
        (should= true (win? gamestate :o)))))

(describe "#has-piece?"
  (it "should return true if the given collection has the piece"
    (should= true (has-piece? [:- :x :o] :x))
    (should= true (has-piece? [:- :x :o] :o))))

(describe "#tied?"
  (let [gamestate {:board (parse-board "xoooxxxxo")}]
    (it "should return true when no one has won and no more spaces are available"
        (should= true (tied? gamestate)))))

(describe "#game-over?"
  (context "x won"
    (let [gamestate {:board (parse-board "xxxo--o--")}]
      (it "should return true when x has won"
        (should= true (game-over? gamestate)))))
  (context "o won"
    (let [gamestate {:board (parse-board "ooox--x--")}]
      (it "should return true when o has won"
        (should= true (game-over? gamestate)))))
  (context "tied game"
    (let [gamestate {:board (parse-board "xoooxxxxo")}]
      (it "should return true when the game has tied"
        (should= true (game-over? gamestate)))))
  (context "game not over"
    (let [gamestate {:board (parse-board "---------")}]
      (it "should return false when the game is not over"
        (should= false (game-over? gamestate))))))

(describe "#possible-moves"
  (it "should return a list of indexes for the possible moves"
    (should= [0 1 2 3 4 5 6 7 8] (possible-moves {:board (parse-board "---------")}))
    (should= [4 5 7 8]           (possible-moves {:board (parse-board "ooox--x--")}))))

(describe "#human-mark"
  (it "should return the humans piece based on the computers piece"
    (should= :x (human-mark {:board (parse-board "---------") :computer :o}))
    (should= :o (human-mark {:board (parse-board "---------") :computer :x}))))

(describe "#add-play-to-board"
  (it "should return an updated board with the current players turn on the board"
    (should= [:x :- :- :- :- :- :- :- :-] (add-play-to-board {:board (parse-board "---------")
                                                              :players [(new-computer :x) (new-human :o)]} 0))))

(describe "#make-next-move"
  (let [human (new-human :x)
        computer (new-computer :o)]
    (it "should take the next move index and put the current players marker on the board and swap to the other players turn"
      (should= {:board [:x :- :- :- :- :- :- :- :-]
                :players [computer human]
                :computer :o
                :options {:difficulty :unbeatable}} (move {:board (parse-board "---------")
                                                           :players [human computer]
                                                           :computer :o
                                                           :options {:difficulty :unbeatable}} 0)))))
