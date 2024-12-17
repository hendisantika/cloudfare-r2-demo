import React, { useState } from "react";
import Axios from "axios";

import { useDropzone } from "react-dropzone";

const fileUploadEndpoint = process.env.REACT_APP_FILE_UPLOAD_ENDPOINT;
const fileDownloadEndpoint = process.env.REACT_APP_FILE_DOWNLOAD_ENDPOINT;

function FileUpload() {
    const [file, setFile] = useState(null);
    const [downloadLink, setDownloadLink] = useState(null);

    const onDrop = (acceptedFiles) => {
        setFile(acceptedFiles[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!file) {
            alert("Please select a file.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
            await Axios.post(fileUploadEndpoint, formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });

            const fileName = encodeURIComponent(file.name);
            const fileDownloadLink = `${fileDownloadEndpoint}/${fileName}`;

            setDownloadLink(fileDownloadLink);

            alert(`${fileName} This has been uploaded successfully.`);

        } catch (error) {
            console.error("Error uploading file: ", error);
            alert("An error occurred while uploading the file.");
        }
    };

    const handleClearFile = () => {
        setFile(null);
        setDownloadLink(null);
    };

    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop});

    return (
        <div>
            <h1>스토리지 파일 업로드</h1>
            <div
                {...getRootProps()}
                style={{
                    width: "100%",
                    height: "200px",
                    border: "2px dashed #ccc",
                    textAlign: "center",
                    lineHeight: "170px",
                    marginBottom: "10px",
                    background: isDragActive ? "#f5f5f5" : "white",
                }}
            >
                <input {...getInputProps()} />
                <p>
                    Import files by dragging and dropping<strong>여기</strong>Click on
                    Please select a file
                </p>
            </div>
            <button
                onClick={() => document.querySelector('input[type="file"]').click()}
            >
                Select file
            </button>
            {file && (
                <div>
                    <h3>Files to upload:</h3>
                    <p>{file.name}</p>
                    <p>
                        <button onClick={handleSubmit}>Upload</button>
                    </p>
                    <p>
                        <button onClick={handleClearFile}>Deselect file</button>
                    </p>
                </div>
            )}
            {downloadLink && (
                <div>
                    <h3>Download uploaded file:</h3>
                    <button onClick={() => window.open(downloadLink, "_blank")}>
                        Download
                    </button>
                </div>
            )}
        </div>
    );
}

export default FileUpload;
