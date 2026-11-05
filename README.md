# cloudfare-r2-demo

[![CI](https://github.com/hendisantika/cloudfare-r2-demo/actions/workflows/ci.yml/badge.svg)](https://github.com/hendisantika/cloudfare-r2-demo/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.16-6DB33F?logo=springboot)
![Gradle](https://img.shields.io/badge/Gradle-9.8.2-02303A?logo=gradle)
![React](https://img.shields.io/badge/React-19-61DAFB?logo=react)
![Dependabot](https://img.shields.io/badge/Dependabot-enabled-025E8C?logo=dependabot)

CloudFare R2 Demo

Implementing file upload/download functionality using Spring Boot, React and Cloudflare R2

Uploading and downloading files is a huge part of what we do on the Internet. Uploading photos taken with friends or
downloading necessary programs is happening constantly right now. In order for us to download files, the files must be
stored in object storage on the Internet.
In this post, we will learn about object storage and actually configure front-end and back-end applications to actually
upload and download files.

### Object storage

Google Cloud defines object storage as follows:
> Object storage is a data storage architecture for storing unstructured data, splitting the data into multiple units (
> objects)
> and storing them in a structurally flat data environment. Each object contains data, metadata, and a unique identifier
> that can be used by applications for easy access and
> retrieval. [Additional documentation](https://cloud.google.com/learn/what-is-object-storage?hl=en)

It may seem complicated, but simply put, it means that we can store various types of files (images, videos, documents,
etc.)
that we have in an external location and access them anytime, anywhere. Google Drive, Dropbox, Naver My Box, etc.,
which are already widely used, can also be seen as examples of object storage. However, in order for application
developers to utilize object storage,
metadata that can identify each file or a protocol that can be accessed via HTTP(S) must be supported. In this case, the
object storage service provided by major CSPs is generally used, and representative products are as follows.

1. AWS Simple Storage Service (AWS3)
2. Azure Blob Storage
3. Google Cloud Storage

Services like the above not only deliver large amounts of data reliably, but also have various security technologies
applied to prevent data from leaking out. In addition, they also have a backup function to prevent data loss. However,
since the cost is determined based on capacity and traffic, you can spend a lot of money depending on the service scale,
file usage method, and file size.

### CDN

All resources used on the Internet are physically stored on a server and downloaded. We are accustomed to using them
wirelessly, but in fact, the Internet is a world built on a huge network of computers connected by cables. If you access
the Submarine Cable Map , which shows you at a glance the submarine cables that cross the world , you will be surprised
at how many cables connect the entire world.
Let's assume one situation. In order to access a service or resource hosted in the United States from Korea, a request
and response are exchanged along the cable. However, because the distance is so far, there is bound to be a delay (
latency) in loading the resource. However, hosting a service in every region of the world is not only astronomical, but
also very difficult to manage. In this case, if you utilize a CDN, you can deliver data at a reasonable cost while
improving speed and stability.
A common way to configure a CDN is to have a network edge in each region and cache the original data. Instead of having
to go to a distant server, the user can receive the data requested from the nearest network edge. CDNs have played a
significant role in enabling us to stream ultra-high definition videos without interruption or buffering.

### Cloudflare

Cloudflare is a cloud-specialized company that provides DDoS defense solutions, CDN (Content Delivery Network), DNS, and
object storage. In particular, CDN can be said to be Cloudflare's core service, and its importance is growing as
large-scale media streaming services such as OTT and SNS are gaining popularity worldwide.

### R2

R2 is an object storage service provided by Cloudflare. As of April 2, 2024, it provides 10 GB of storage space per
month for free, and its biggest advantage is that you can use AWS S3's SDK as is. In addition, it provides integrated
Cloudflare's Global DNS, so you can get data from the network edge closest to the user without any separate settings. In
the following exercises, we will configure a simple file upload/download application with Spring Boot, React, and R2.

CloudFare R2 Architecture

![CloudFare Architecture](img/arch.png "CloudFare Architecture")

### Version information

* Backend
    * JDK 25
    * Spring Boot 3.5.16
    * Gradle 9.8.2 (via the bundled Gradle wrapper)
    * AWS SDK for Java (S3) v1 & v2
* Frontend
    * Node.js 24 (LTS)
    * pnpm 12
    * React 19
* Preparation
    * Device with JDK 25 installed
    * Device with Node.js 24 and pnpm 12 installed
    * GitHub account
    * Cloudflare Account
* GitHub Repository
  The hands-on training will be done using the Spring Boot, React application in the repository below. Please clone or
  fork the repository.

https://github.com/hendisantika/cloudfare-r2-demo

### Follow along

Generate Cloudflare R2 API Token

1. After logging in to Cloudflare, go to the R2 dashboard. Then, click the R2 API Token Management button under your
   Account ID in the upper right corner.

![CloudFare Object Storage](img/r2-object-storage.png "CloudFare Object Storage")

2. Click the Generate API Token button in the upper right corner.

![CloudFare Object Token](img/r2-token.png "CloudFare Object Storage Token")

3. Name the token freely as you like and select Administrator Read and Write for the permissions. Select the desired
   token activation period for TTL and press the Generate API Token button.
4. Once token generation is complete, the account information and endpoint required for client use are displayed. In
   this exercise, the S3 client access information and endpoint are used. If you leave the page, the authentication
   information cannot be retrieved again, so store the values ​​in a safe place. The correspondence with AWS S3 is as
   follows.
   Access Key ID => AWS Access Key ID
   Secret Access Key => AWS Secret Key
   Endpoint => AWS S3 Endpoint URL

![CloudFare Token](img/R2%20Object%20Storage_Token.png "CloudFare Token")

### Create a Cloudflare R2 API Bucket

1. Return to the R2 page in your dashboard and click the Create Bucket button.
2. Enter an appropriate bucket name and the location will be automatically selected. Then press the Create Bucket
   button.
3. After bucket creation is complete, you can check the information by accessing the Settings tab as follows. The APAC
   displayed in the Location field is used as the Region value in the S3 client.

### Configure & run the backend

The backend reads its R2 settings from environment variables (see `backend/src/main/resources/application.yml`).
Never commit real credentials — export them in your shell instead:

```shell
export AWS_S3_REGION=APAC
export AWS_ACCESS_KEY=<R2 Access Key ID>
export AWS_SECRET_KEY=<R2 Secret Access Key>
export AWS_S3_BUCKET=<bucket name>
export AWS_S3_ENDPOINT_URL=https://<account-id>.r2.cloudflarestorage.com

cd backend
./gradlew bootRun      # run the app
./gradlew build        # compile + test (requires JDK 25)
```

### Run the frontend

```shell
cd frontend
pnpm install --frozen-lockfile
pnpm start             # dev server on http://localhost:3000
pnpm run build         # production build
```

### Continuous Integration

Every push and pull request to `main` runs the [CI workflow](.github/workflows/ci.yml) on GitHub Actions:

| Job      | Toolchain                       | Steps                                            |
|----------|---------------------------------|--------------------------------------------------|
| Backend  | Temurin JDK 25, Gradle 9.8.2    | `./gradlew build` (compile + unit tests)         |
| Frontend | Node.js 24, pnpm 12             | `pnpm install --frozen-lockfile`, `pnpm run build` |

### Dependency updates (Dependabot)

[Dependabot](.github/dependabot.yml) checks for updates every Monday at 06:00 (Asia/Jakarta) and opens grouped PRs:

* **Gradle** (`/backend`) — Spring Boot, AWS SDK and other minor/patch updates are grouped.
* **npm** (`/frontend`, pnpm lockfile) — React, production and development dependencies are grouped.
* **GitHub Actions** — all action version bumps in a single PR.

PRs are labelled (`dependencies`, `backend`, `frontend`, `github-actions`) and use conventional commit prefixes
(`chore(deps)`, `chore(deps-dev)`, `ci(deps)`).
