# big-data-analysiss
Projede beklenildiği üzere bir real-time monitöring sistemi geliştirilmiştir. Java kullanılarak 3 microservisten oluşan program yazılmıştır. Real-time monitöring işlemi için ise Prometheus aracı kullanılmıştır.

Genel olarak özetleyecek olursak;

log-creator: İstenen formatta log dosyalarini olusturup /project/raw altina *.log seklinde atiyor.

log-watcher: /project/raw pathini dinliyor ve yeni dosya oluşturulduğu zaman okuyup kafka'da log topigine atiyor

log-consumer: Kafka'da log topiğini dinliyor ve her aldığı mesaji parse edip metric kaydediyor. prometheus: log-consumer servisinden metricleri çekiyor.

Çalıştırılması için aşağıdaki komutu kullanmanız yeterli olacaktır.

  mkdir -p /project/raw && docker-compose up -d
  
Sonrasında web browser'da IP:9090 yazdığınızda prometheus başlatılır. Execute butonunun yanındaki combobox'tan istenilen metric çekilip monitör edilebilir.

Birden fazla defa çalıştırılması durumunda log klasörünü temizlemekte fayda vardır. Komutu şu şekildedir:

  rm -rf /project/raw/*
  
Servislerin log'ları incelenmek istenirse şu komutlar kullanılmalıdır:

  docker-compose logs log-watcher
  docker-compose logs log-creator
  docker-compose logs log-consumer
  
Bunlara ek olarak docker hub'dan imajlar çekilmek istenirse aşağıdaki komutları kullanabilirsiniz:

  docker pull aysenuroruc/log-creator
  docker pull aysenuroruc/log-watcher
  docker pull aysenuroruc/log-consumer
  docker pull aysenuroruc/prometheus
  
Tüm bunlara ek olarak, eğer Vagrant (VM)'da çalıştırmak isterseniz aşağıdaki adımları takip ediniz:

  vagrant up
  vagrant ssh 
  web browser'dan 10.10.3.20:9090 adresine gidiniz. 