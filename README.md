# Dağıtık Abonelik Sistemi (Distributed Subscriber Service)

### plotter.py

- [x] admin.rb'den kapasite değerlerini alır.
- [x] admin.rb ile socket üzerinden iletişim kurar.
- [x] gelen kapasite değerlerini grafiğe döker.

### admin.rb

- [x] serverlar ile socket üzerinden iletişim kurar.
- [x] serverlara 5 saniyede bir kapasite sorgusu gönderir.
- [x] serverlara birbirlerine bağlanmak için strt emri gönderir.
- [x] strt emrine karşılık gelen mesaj isteklerini alır.
- [x] kapasite sorgusuna karşılık gelen kapasite değerlerini alır.

### ServerX.java

- [x] admin.rbden gelen kapasite sorgusunu alır.
- [x] gelen kapasite sorgusuna karşılık kapasite değerleri gönderir.
- [x] admin.rbden gelen strt emrini alır.
- [x] gelen strt emrine karşılık yep veya nope mesajları gönderir.
- [x] istemcilerle socketler üzerinden haberleşir.
- [x] istemcilerden subcriber nesnsi alır.
