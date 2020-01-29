(ns todo-app.profile.validations)

(defn valid?
  [profile]
  (and (string? (:name profile))
       (> (count (:name profile)) 0)))