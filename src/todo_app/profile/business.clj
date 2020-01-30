(ns todo-app.profile.business
  (:require [todo-app.profile.repository :as repo]
            [todo-app.profile.validations :refer [valid?]]))

(defn filter-profiles
  [pred]
  (filter pred (repo/get-profiles)))

(defn get-by-id
  [id]
  (first (filter-profiles #(= id (:id %)))))

(defn add!
  [profile]
  (if (valid? profile)
    (repo/add! profile)
    "Invalid profile"))