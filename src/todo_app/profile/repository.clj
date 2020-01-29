(ns todo-app.profile.repository)

(defrecord Profile [id name creation-date])
(def profiles (atom []))

(defn order-by
  [pred]
  (filter pred @profiles))

(defn add!
  [profile]
  (let [{name :name} profile
        id (inc (count @profiles))
        date (java.util.Date.)]
    (swap! profiles conj (->Profile id name date)))
  (last @profiles))

(defn profiles-thing []
  @profiles)