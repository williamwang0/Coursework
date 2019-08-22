; Lab 13: Final Review

(define (compose-all funcs)
  (if (null? funcs)
  	(lambda (x) x)
  	(lambda (x) ((car funcs) (compose-all (cdr funcs)))))
)