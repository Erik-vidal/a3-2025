FROM ubuntu:22.04

# Evitar prompts interativos durante a instalação
ENV DEBIAN_FRONTEND=noninteractive

# Instalando Java, Maven e dependências do X11/GUI
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven \
    libx11-6 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libxrandr2 \
    libxinerama1 \
    libgl1-mesa-glx \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .

# Configurando variáveis de ambiente
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV DISPLAY=:0

# Compilar e executar o aplicativo
CMD ["mvn", "spring-boot:run"] 