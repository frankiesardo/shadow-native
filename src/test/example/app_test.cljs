(ns example.app-test
  (:require
    ["react" :rename {createElement $}]
    ["react-test-renderer" :as renderer]
    [example.app :as app]))

(js/test
  "Adds 1 + 2 to equal 3"
  #(.. (js/expect (+ 1 2)) (toBe 3)))

;(js/it "Matches Root Snapshot"
;  (fn []
;    (let [tree (.. renderer (create ($ app/Parent)) toJSON)]
;      (.. (js/expect tree) (toMatchSnapshot)))))
