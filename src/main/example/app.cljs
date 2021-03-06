(ns example.app
  (:require
    ["expo" :as ex]
    ["react-native" :as rn]
    ["react" :as react :rename {createElement $}]
    ["react-native-paper" :as p]
    ["react-test-renderer" :as renderer]
    ["use-reducer-with-effects" :refer [useReducerWithEffects]]
    [react.wrapper :refer-macros [defc]]
    [shadow.expo :as expo]))

;; must use defonce and must refresh full app so metro can fill these in
;; at live-reload time `require` does not exist and will cause errors
;; must use path relative to :output-dir

(defonce splash-img (js/require "../../res/shadow-cljs.png"))

(set! *warn-on-infer* true)

(defmulti reducer (fn [state {:keys [type payload]}] type))

(defn reducerWithEffects [state action] (reducer state action))

(defmethod reducer ::login [state {{:keys [email password]} :payload :as action}]
  #js {:state "Loading..."
       :http {:url :login-url
              :params (str email ":" password)
              :callback ::login-cb}
       :log action})

(defmethod reducer ::login-cb [state {{:keys [success body]} :payload :as action}]
  #js {:state (if success
                (str "Your name is " (get-in body [:profile :name]))
                "Call to server failed")
       :log action})

(defmethod reducer ::logout [state action]
  #js {:state "Loading..."
       :http {:url :logout-url
              :callback ::logout-cb}
       :log action})

(defmethod reducer ::logout-cb [state {{:keys [success]} :payload :as action}]
  #js {:state "Logged out"
       :log action})

(defn http [dispatch {:keys [callback] :as payload}]
  (js/setTimeout
    #(do (println "Calling server.." payload)
         (dispatch {:type callback
                    :payload {:success true
                              :status 200
                              :body {:profile {:name "Alice"}}}}))
    1000))

(defn log [dispatch payload]
  (println payload))

(def effect-handlers #js {:http http :log log})

(def initial-state "Logged out")

(def DispatchContext (react/createContext))

(defc Navigator [state]
  (let [dispatch (react/useContext DispatchContext)]
    ($ rn/View #js {:style #js {:flex 1}}
       ($ rn/Text #js {} (str "Current status: " state))
       ($ rn/Button #js {:title "Login"
                         :onPress #(dispatch {:type ::login
                                              :payload {:email "foo@gmail.com"
                                                        :password "123"}})})
       ($ rn/Button #js {:title "Logout"
                         :onPress #(dispatch {:type ::logout})}))))

(defc App []
  (let [[state dispatch] (useReducerWithEffects reducerWithEffects effect-handlers initial-state)]
    ($ DispatchContext.Provider #js {:value dispatch}
       ($ p/Provider nil (Navigator state)))))

(defn start
  {:dev/after-load true}
  []
  (expo/render-root (App)))

(defn init []
  (start))
