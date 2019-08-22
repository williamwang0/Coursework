(define (accumulate combiner start n term)
  (cond 
  	((= n 0) start)
  	(else (combiner (term n) (accumulate combiner start (- n 1) term)))
  )
)

(define (accumulate-tail combiner start n term)
  (cond 
  	((= n 0) start)
  	(else (accumulate-tail combiner (combiner start (term n)) (- n 1) term))
  )
)

(define (rle s)
  (define (help lst lastnum cnt)
  	(cond 
  		((null? lst) (cons-stream (list lastnum cnt) nil))
  		((= lastnum (car lst)) (help (cdr-stream lst) lastnum (+ cnt 1)))
  		(else (cons-stream (list lastnum cnt) (help (cdr-stream lst) (car (cdr-stream lst)) 0)))
  	)
  )
  (if (null? s)
  	()
  	(help s (car s) 0)
  )
)