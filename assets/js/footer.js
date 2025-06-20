// Social links
const socialLinks = [
    { href: "https://github.com/s23010843/essentials-monitor", icon: "fab fa-github" }
];

const socialLinksContainer = document.getElementById('footer-social-links');
socialLinks.forEach(link => {
    const a = document.createElement('a');
    a.href = link.href;
    a.className = 'social-link';
    a.innerHTML = `<i class="${link.icon}"></i>`;
    socialLinksContainer.appendChild(a);
});

// Footer links data
const footerLinks = {
    product: [
        { href: "#features", text: "Features" },
        { href: "#screenshots", text: "Screenshots" },
        { href: "#download", text: "Download" },
        { href: "#", text: "Pricing" }
    ],
    support: [
        { href: "#contact", text: "Contact" },
        { href: "#", text: "Help Center" },
        { href: "#", text: "Privacy Policy" },
        { href: "#", text: "Terms of Service" }
    ],
    company: [
        { href: "/", text: "About Us" },
        { href: "#", text: "Careers" },
        { href: "#", text: "Press" },
        { href: "/blog", text: "Blog" }
    ]
};

// Helper to render links
function renderFooterLinks(containerId, links) {
    const container = document.getElementById(containerId);
    links.forEach(link => {
        const li = document.createElement('li');
        const a = document.createElement('a');
        a.href = link.href;
        a.textContent = link.text;
        li.appendChild(a);
        container.appendChild(li);
    });
}

renderFooterLinks('footer-product-links', footerLinks.product);
renderFooterLinks('footer-support-links', footerLinks.support);
renderFooterLinks('footer-company-links', footerLinks.company);