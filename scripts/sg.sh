sbt "runMain org.krz.SocieteGenerale data/sg-in.csv" | sed '1,2d' > data/sg-out.csv 
