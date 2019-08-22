(define (accumulate combiner start n term)
  (if (> n 0)
  	(combiner (term n) (accumulate combiner start (- n 1) term))
  	start
  	)
)

(define (accumulate-tail combiner start n term)
  (if (= n 0)
  	start
  	(accumulate-tail combiner (combiner (term n) start) (- n 1) term)
  	)
)

(define (rle s)
  (define (rle-helper prev count s)
   (cond
    ((null? s) (cons-stream (list prev count) nil))
    ((= (car s) prev) (rle-helper prev (+ 1 count) (cdr-stream s)))
    (else (cons-stream (list prev count) (rle-helper (car s) 1 (cdr-stream s))))
   )
 )
 (if (null? s)
  nil
  (rle-helper (car s) 1 (cdr-stream s))
 )

  ; (define (helper lst result currCount currNum)
  ; 	(if (null? lst)
  ; 		nil
  ; 		(if (= currNum (car lst))
  ; 		(helper (cdr lst) result (+ currCount 1) currNum)
  ; 		(helper (cdr lst) (append result (cons currNum currCount)) 0 (car (cdr lst)))
  ; 		)
  ; 			)
  ; 		)
  ; (if (null? s)
  ; 	(helper s nil 0 0)
  ; 	(helper s nil 0 (car s))
  ; 	)
)