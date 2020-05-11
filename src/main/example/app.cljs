(ns example.app
  (:require
    ["expo" :as ex]
    ["react-native" :as rn]
    ["react" :as react :rename {createElement $}]
    ["react-native-paper" :as p]
    ["react-test-renderer" :as renderer]
    [react.wrapper :refer-macros [defc]]
    [shadow.expo :as expo]))

;; must use defonce and must refresh full app so metro can fill these in
;; at live-reload time `require` does not exist and will cause errors
;; must use path relative to :output-dir

(defonce splash-img (js/require "../../res/shadow-cljs.png"))

(set! *warn-on-infer* true)

(def styles ^js
  (-> {:container
       {:flex            1
        :backgroundColor "#fff"
        :alignItems      "center"
        :justifyContent  "center"}
       :title
       {:fontWeight "bold"
        :fontSize   24
        :color      "blue"}}
      clj->js
      rn/StyleSheet.create))

(defc Child [a]
  ($ rn/View #js {:style (.-container styles)}
     ($ rn/Text #js {:style (.-title styles)} a)
     ($ p/Button #js {:onPress #(println ::clicked)} "Press me")
     ($ rn/Image #js {:source splash-img :style #js {:width 200 :height 200}})))

(defn Parent []
  ($ p/Provider nil
     (Child "Hi")))

(defn start
  {:dev/after-load true}
  []
  (expo/render-root Parent))

(defn init []
  (start))

