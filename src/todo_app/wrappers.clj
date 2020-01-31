(ns todo-app.wrappers)

(defn wrap-logger
  [handler]
  (fn [request]
    (spit "test.txt" (:body request))
    (handler request)))

(defn wrap-validate
  [handler validate]
  (fn [request]
    (if (validate request)
      {:status 422 :body ""}
      (handler request))))

; (defroutes t
;   (wrap-validate (POST "/" req (tasks/add! (:body req))) (fn [_] false)))