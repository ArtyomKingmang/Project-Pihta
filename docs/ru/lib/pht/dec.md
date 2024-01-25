## inc
__Декриментирует__ значение `переменной`.<br>
`--`/`dec` - сначала декрементируют переменную, после чего возвращает её значение.<br>
`dec-` - сначала возвращает значение переменной, после чего декрементирует её.

### Применение
1. `(-- var)`<br>
`var` - _переменная_.
2. `(dec var)`<br>
`var` - _переменная_.
3. `(dec- var)`<br>
`var` - _переменная_.

### Примеры

```pihta
(use-ctx pht
    (app-fn
        (println (body (def [[i 0]]) (--   i)))
        (println (body (def [[i 0]]) (dec  i)))
        (println (body (def [[i 0]]) (dec- i)))
```