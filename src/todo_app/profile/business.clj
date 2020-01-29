(ns todo-app.profile.business
  (:require [todo-app.profile.repository :as repo]
            [todo-app.profile.validations :refer [valid?]]
            [todo-app.helper :refer [response-error]]
            [ring.util.response :refer [response]]))

(defn filter-profiles
  [pred]
  (filter pred (repo/get-profiles)))

(defn get-by-id
  [id]
  (first (filter-profiles #(= id (:id %)))))

(defn add!
  [profile]
  (if (valid? profile)
    (response (repo/add! profile))
    (response-error 422 "Invalid profile")))