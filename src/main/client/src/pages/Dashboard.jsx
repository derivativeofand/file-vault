import { useState, useEffect } from 'react';

import api from '../axios';

function Dashboard() {
    const [files, setFiles] = useState([]);
    const [selectedFile, setSelectedFile] = useState(null);

    async function fetchFiles() {
            try {
                // Send a GET request to the backend to fetch the list of files, including the authorization token in the headers.
                const response = await api.get('/files', { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
                console.log('Fetched files:', response.data);
                if(response.status === 200) {
                    setFiles(response.data);
                }
            } catch (error) {
                console.error('Failed to fetch files:', error);
            }
    }

    async function handleUpload() {
        if(!selectedFile) {
            console.error('No file selected for upload.');
            return;
        } else{
            const formData = new FormData();
            formData.append('file', selectedFile);
            try {
                const response = await api.post('/files', formData, { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
                if(response.status === 200) {
                    console.log('File uploaed successfully.');
                    fetchFiles(); // Refresh the list of files after a successful upload.
                }
            } catch (error) {
                console.error('Failed to upload file:', error);
            }
        }
    }

    useEffect(() => {
        fetchFiles();   
    }, []);

    return (
        <div>
            <h1>Dashboard</h1><br></br>
            <h2>Uploaded Files</h2>
            
            {/* // Display the list of uploaded files. Each file is represented as a list item. */}
            <ul>
                {files.map((file) => (
                    <li key={file.id}>File Name: {file.name}</li>
                ))}
            </ul><br></br>
            <label htmlFor="file-upload">Upload a file:</label>
            <input type="file" id="file-upload" onChange={(e) => setSelectedFile(e.target.files[0])} /><br></br>
            <button onClick={handleUpload}>Upload</button><br></br>
        </div>
        
    )
}

export default Dashboard;
        