(ns todo-app.profile.business
  (:require [todo-app.profile.repository :as repo]))

(defn add!
  [profile]
  (repo/add! profile))