(ns todo-app.profile.business
  (:require [todo-app.profile.repository :as repo]))

(defn filter-profiles
  [pred]
  (repo/order-by pred))

(defn get-by-id
  [id]
  (first (filter-profiles #(= id (:id %)))))

(defn add!
  [profile]
  (repo/add! profile))