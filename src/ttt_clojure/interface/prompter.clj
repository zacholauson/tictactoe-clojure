(ns ttt-clojure.interface.prompter)

(defprotocol Prompter
  (ask                  [this output-str option-collection])
  (ask-for-move         [this gamestate]))
