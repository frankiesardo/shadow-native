(ns react.wrapper
  #?(:cljs (:require ["react" :as react]
                     [goog.object :as obj])))

#?(:cljs

   (defn component [display-name render]
     (let [f (fn [props] (apply render (obj/get props "value")))
           m (react/memo f #(= (obj/get %1 "value") (obj/get %2 "value")))]
       (obj/set f "displayName" display-name)
       (fn create-element [& props] (react/createElement m #js {:value props}))))

   :clj

   (defmacro defc [name argv & body]
     `(def ~name (react.wrapper/component (name '~name) (fn ~argv ~@body)))))