# --- Giai đoạn 1: Build ứng dụng với Maven ---
# Sử dụng một image chứa JDK 21 và Maven để build file .jar, tương thích với cấu hình dự án hiện tại.
FROM maven:3-eclipse-temurin-21 AS build

# Thiết lập thư mục làm việc bên trong container
WORKDIR /app

# Sao chép file pom.xml và tải các dependency
# Tận dụng cache của Docker để không phải tải lại nếu dependency không đổi
COPY pom.xml .
RUN mvn dependency:go-offline

# Sao chép toàn bộ mã nguồn và build ứng dụng, bỏ qua tests để build nhanh hơn
COPY src ./src
RUN mvn clean package -DskipTests

# --- Giai đoạn 2: Chạy ứng dụng ---
# Sử dụng một image JRE 21 gọn nhẹ hơn để chạy, giúp tối ưu kích thước
FROM eclipse-temurin:21-jre

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file .jar đã được build từ giai đoạn 'build' và đổi tên thành app.jar cho đơn giản
COPY --from=build /app/target/smartrecruit-0.0.1-SNAPSHOT.jar app.jar

# Cổng mà ứng dụng Spring Boot sẽ chạy (mặc định là 8080)
# Render sẽ tự động map cổng này ra ngoài
EXPOSE 8080

# Lệnh để khởi chạy ứng dụng khi container bắt đầu
ENTRYPOINT ["java", "-jar", "app.jar"]
