FROM ubuntu:24.04

# Set timezone to avoid interactive prompts during package installation
RUN ln -fs /usr/share/zoneinfo/Etc/UTC /etc/localtime && \
    echo "Etc/UTC" > /etc/timezone

# Update package lists and install basic utilities
RUN DEBIAN_FRONTEND=noninteractive apt-get update && \
    apt-get install -y \
        curl \
        wget \
        vim \
        nano \
        git \
        build-essential \
        software-properties-common \
        apt-transport-https \
        ca-certificates \
        gnupg \
        lsb-release \
        sudo \
        net-tools \
        iputils-ping \
        telnet \
        unzip \
        zip \
        jq \
        htop \
        tree \
        less \
    && rm -rf /var/lib/apt/lists/*

    # Install Java and Maven.
ENV JAVA_HOME="/usr/lib/jvm/java-11-openjdk-arm64"
ENV PATH="${JAVA_HOME}/bin:${PATH}"

RUN DEBIAN_FRONTEND=noninteractive && \
    apt-get update && \
    apt-get install -y openjdk-11-jdk maven && \
    rm -rf /var/lib/apt/lists/* && \
    java --version && \
    mvn --version

# Install Python and pip.
RUN DEBIAN_FRONTEND=noninteractive && \
    apt-get update && \
    apt-get install -y software-properties-common && \
    add-apt-repository ppa:deadsnakes/ppa && \
    apt-get update && \
    apt-get install -y python3.9-dev python3.9-venv python3-pip jq && \
    rm -rf /var/lib/apt/lists/* && \
    echo "alias python='python3.9'" >> ~/.bashrc && \
    echo "alias pip='python3.9 -m pip'" >> ~/.bashrc

# Install Python packages using virtual environment
RUN python3.9 -m venv /opt/flask-env && \
    /opt/flask-env/bin/pip install flask && \
    echo 'export PATH="/opt/flask-env/bin:$PATH"' >> ~/.bashrc

# Install .NET Core SDK.
RUN curl https://packages.microsoft.com/config/ubuntu/20.04/packages-microsoft-prod.deb -o packages-microsoft-prod.deb && \
    dpkg -i packages-microsoft-prod.deb && \
    rm packages-microsoft-prod.deb && \
    apt-get update && \
    apt-get install -y dotnet-sdk-8.0

# Install Node.js
RUN curl -fsSL https://deb.nodesource.com/setup_22.x -o nodesource_setup.sh && \
    bash nodesource_setup.sh && \
    apt-get install -y nodejs && \
    rm nodesource_setup.sh


# Install Contrast Flex Agent (configurable)
ARG AGENT_TOKEN
RUN curl https://pkg.contrastsecurity.com/api/gpg/key/public | gpg -o /usr/share/keyrings/contrast-keyring.pgp --dearmor && \
    echo "deb [signed-by=/usr/share/keyrings/contrast-keyring.pgp] https://pkg.contrastsecurity.com/debian-public/ $(sed -rne 's/^VERSION_CODENAME=(.*)$/\1/p' /etc/*ease) contrast" | tee /etc/apt/sources.list.d/contrast.list && \
    echo "deb [signed-by=/usr/share/keyrings/contrast-keyring.pgp] https://pkg.contrastsecurity.com/debian-public/ all contrast" | tee -a /etc/apt/sources.list.d/contrast.list && \
    apt-get update && \
    if [ -n "$AGENT_TOKEN" ]; then \
        echo "Installing Contrast Flex Agent with provided token..."; \
        AGENT_TOKEN="$AGENT_TOKEN" apt-get install -y contrast-flex-agent; \
    else \
        echo "No AGENT_TOKEN provided, skipping Contrast Flex Agent installation"; \
    fi
# To install the Contrast Flex Agent, provide AGENT_TOKEN as a build argument


# Create demos directory and set as working directory
RUN mkdir -p /demos
WORKDIR /demos

# Set execute permissions on scripts when copied to container
# This ensures scripts work regardless of host OS permissions
RUN echo '#!/bin/bash' > /usr/local/bin/fix-permissions.sh && \
    echo 'chmod +x /DEMO/*.sh 2>/dev/null || true' >> /usr/local/bin/fix-permissions.sh && \
    echo 'chmod +x /DEMO/demo-control.sh 2>/dev/null || true' >> /usr/local/bin/fix-permissions.sh && \
    chmod +x /usr/local/bin/fix-permissions.sh

# Keep container running
CMD ["/bin/bash", "-c", "/usr/local/bin/fix-permissions.sh && tail -f /dev/null"]
