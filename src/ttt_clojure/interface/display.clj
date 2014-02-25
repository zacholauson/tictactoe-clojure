(ns ttt-clojure.interface.display)

(defprotocol Display
  (output         [this output-string])
  (print-board    [this board])
  (declare-winner [this winning-statement]))
