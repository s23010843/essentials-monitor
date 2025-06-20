// screenshot.js (this file)
fetch('./assets/json/screenshots.json')
    .then(response => response.json())
    .then(screenshots => {
        const gallery = document.getElementById('screenshots-gallery');
        screenshots.forEach(screenshot => {
            const item = document.createElement('div');
            item.className = 'screenshot-item';
            item.innerHTML = `
                <img src="${screenshot.src}" alt="${screenshot.alt}" class="screenshot-img">
                <div class="screenshot-overlay">
                    <i class="fas fa-expand"></i>
                </div>
            `;
            gallery.appendChild(item);
        });
    });