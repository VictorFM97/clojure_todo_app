(ns todo-app.helper)

(defn response-error
  [status message]
  {:status status :body message})