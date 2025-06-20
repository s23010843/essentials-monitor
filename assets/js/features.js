const features = [
    {
        icon: "fas fa-brain",
        title: "AI-Powered Intelligence",
        description: "Smart task prioritization, automated scheduling, and intelligent reminders that learn from your habits.",
        image: "https://images.unsplash.com/photo-1677442136019-21780ecad995?w=400&h=300&fit=crop",
        alt: "AI Intelligence Feature"
    },
    {
        icon: "fas fa-users",
        title: "Team Collaboration",
        description: "Real-time collaboration with team members, shared projects, and seamless communication tools.",
        image: "https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=400&h=300&fit=crop",
        alt: "Team Collaboration Feature"
    },
    {
        icon: "fas fa-chart-line",
        title: "Analytics & Insights",
        description: "Track your productivity patterns with detailed analytics and get personalized insights to improve efficiency.",
        image: "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=400&h=300&fit=crop",
        alt: "Analytics Feature"
    },
    {
        icon: "fas fa-cloud",
        title: "Cloud Synchronization",
        description: "Access your tasks from anywhere with secure cloud sync across all your devices and platforms.",
        image: "https://images.unsplash.com/photo-1544197150-b99a580bb7a8?w=400&h=300&fit=crop",
        alt: "Cloud Sync Feature"
    }
];

const featuresGrid = document.getElementById('features-grid');
features.forEach(feature => {
    const card = document.createElement('div');
    card.className = 'feature-card';
    card.innerHTML = `
        <div class="feature-icon">
            <i class="${feature.icon}"></i>
        </div>
        <div class="feature-content">
            <h3 class="feature-title">${feature.title}</h3>
            <p class="feature-description">${feature.description}</p>
            <img src="${feature.image}" alt="${feature.alt}" class="feature-image">
        </div>
    `;
    featuresGrid.appendChild(card);
});