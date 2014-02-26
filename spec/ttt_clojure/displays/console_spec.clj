(ns ttt-clojure.displays.console-spec
  (:require [speclj.core                   :refer :all]
            [ttt-clojure.displays.console  :refer :all]
            [ttt-clojure.interface.display :refer :all]))

(def display (new-display-console))

(describe "#index-board"
  (it "should return an indexed board when given an empty board"
    (should= [" 0" " 1" " 2" " 3" " 4" " 5" " 6" " 7" " 8"] (index-board [:- :- :- :- :- :- :- :- :-])))
  (it "should return a board with indexes where there is not a player mark"
    (should= [" x" " x" " 2" " 3" " 4" " 5" " 6" " o" " o"] (index-board [:x :x :- :- :- :- :- :o :o]))))

(describe "#row-size"
  (it "should return the row size of the given board"
    (should= 3 (row-size [:- :- :- :- :- :- :- :- :-]))
    (should= 4 (row-size [:- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-]))
    (should= 5 (row-size [:- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :- :-]))))

(describe "Console"
  (it "should output the given string"
    (should= "Hello\n"
      (with-out-str (output display "Hello")))))
