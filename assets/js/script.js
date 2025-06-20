//Current Year
document.getElementById("current-year").textContent = new Date().getFullYear();

// DOM Content Loaded Event
document.addEventListener('DOMContentLoaded', function() {
    initializeNavigation();
    initializeScrollAnimations();
    initializeImageModal();
    initializeContactForm();
    initializeSmoothScrolling();
});

// Navigation functionality
function initializeNavigation() {
    const navToggle = document.getElementById('nav-toggle');
    const navMenu = document.getElementById('nav-menu');
    const navLinks = document.querySelectorAll('.nav-link');

    // Toggle mobile menu
    navToggle.addEventListener('click', function() {
        navMenu.classList.toggle('active');
        navToggle.classList.toggle('active');
    });

    // Close mobile menu when clicking on a link
    navLinks.forEach(link => {
        link.addEventListener('click', function() {
            navMenu.classList.remove('active');
            navToggle.classList.remove('active');
        });
    });

    // Close mobile menu when clicking outside
    document.addEventListener('click', function(event) {
        const isClickInsideNav = navMenu.contains(event.target) || navToggle.contains(event.target);
        if (!isClickInsideNav && navMenu.classList.contains('active')) {
            navMenu.classList.remove('active');
            navToggle.classList.remove('active');
        }
    });

    // Navbar scroll effect
    window.addEventListener('scroll', function() {
        const navbar = document.querySelector('.navbar');
        if (window.scrollY > 50) {
            navbar.style.background = 'rgba(255, 255, 255, 0.98)';
            navbar.style.boxShadow = '0 2px 20px rgba(0, 0, 0, 0.1)';
        } else {
            navbar.style.background = 'rgba(255, 255, 255, 0.95)';
            navbar.style.boxShadow = 'none';
        }
    });

    // Active navigation link highlighting
    window.addEventListener('scroll', highlightActiveNavLink);
}

// Highlight active navigation link based on scroll position
function highlightActiveNavLink() {
    const sections = document.querySelectorAll('section[id]');
    const navLinks = document.querySelectorAll('.nav-link');
    
    let current = '';
    
    sections.forEach(section => {
        const sectionTop = section.offsetTop;
        const sectionHeight = section.clientHeight;
        if (window.scrollY >= (sectionTop - 200)) {
            current = section.getAttribute('id');
        }
    });

    navLinks.forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href') === '#' + current) {
            link.classList.add('active');
        }
    });
}

// Smooth scrolling for anchor links
function initializeSmoothScrolling() {
    const links = document.querySelectorAll('a[href^="#"]');
    
    links.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            const targetId = this.getAttribute('href');
            const targetSection = document.querySelector(targetId);
            
            if (targetSection) {
                const offsetTop = targetSection.offsetTop - 70; // Account for fixed navbar
                
                window.scrollTo({
                    top: offsetTop,
                    behavior: 'smooth'
                });
            }
        });
    });
}

// Scroll animations
function initializeScrollAnimations() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('animate-slide-up');
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);

    // Observe elements for animation
    const elementsToAnimate = document.querySelectorAll([
        '.feature-card',
        '.screenshot-item',
        '.contact-item',
        '.hero-text',
        '.hero-image'
    ].join(', '));

    elementsToAnimate.forEach(element => {
        observer.observe(element);
    });

    // Counter animation for hero stats
    animateCounters();
}

// Animate counter numbers in hero section
function animateCounters() {
    const counters = document.querySelectorAll('.stat-number');
    
    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                animateCounter(entry.target);
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.5 });

    counters.forEach(counter => {
        observer.observe(counter);
    });
}

function animateCounter(element) {
    const target = element.textContent;
    const isRating = target.includes('★');
    const isKPlus = target.includes('K+');
    const isMPlus = target.includes('M+');
    
    let finalNumber;
    let suffix = '';
    
    if (isRating) {
        finalNumber = parseFloat(target.replace('★', ''));
        suffix = '★';
    } else if (isKPlus) {
        finalNumber = parseInt(target.replace('K+', ''));
        suffix = 'K+';
    } else if (isMPlus) {
        finalNumber = parseInt(target.replace('M+', ''));
        suffix = 'M+';
    } else {
        finalNumber = parseInt(target);
    }
    
    let currentNumber = 0;
    const increment = finalNumber / 50;
    const timer = setInterval(() => {
        currentNumber += increment;
        if (currentNumber >= finalNumber) {
            currentNumber = finalNumber;
            clearInterval(timer);
        }
        
        if (isRating) {
            element.textContent = currentNumber.toFixed(1) + suffix;
        } else {
            element.textContent = Math.floor(currentNumber) + suffix;
        }
    }, 30);
}

// Image modal functionality
function initializeImageModal() {
    const modal = document.getElementById('image-modal');
    const modalImage = document.getElementById('modal-image');
    const closeBtn = document.querySelector('.close');
    const screenshotItems = document.querySelectorAll('.screenshot-item');

    // Open modal when clicking on screenshot
    screenshotItems.forEach(item => {
        item.addEventListener('click', function() {
            const img = this.querySelector('.screenshot-img');
            modal.style.display = 'block';
            modalImage.src = img.src;
            modalImage.alt = img.alt;
            document.body.style.overflow = 'hidden';
        });
    });

    // Close modal
    function closeModal() {
        modal.style.display = 'none';
        document.body.style.overflow = 'auto';
    }

    closeBtn.addEventListener('click', closeModal);

    // Close modal when clicking outside the image
    modal.addEventListener('click', function(event) {
        if (event.target === modal) {
            closeModal();
        }
    });

    // Close modal with Escape key
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape' && modal.style.display === 'block') {
            closeModal();
        }
    });
}

// Contact form functionality
function initializeContactForm() {
    const contactForm = document.getElementById('contact-form');
    
    contactForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // Get form data
        const formData = new FormData(contactForm);
        const formObject = {};
        
        formData.forEach((value, key) => {
            formObject[key] = value;
        });
        
        // Validate form
        if (validateContactForm(formObject)) {
            // Simulate form submission
            submitContactForm(formObject);
        }
    });
}

// Validate contact form
function validateContactForm(data) {
    const errors = [];
    
    if (!data.name || data.name.trim().length < 2) {
        errors.push('Name must be at least 2 characters long');
    }
    
    if (!data.email || !isValidEmail(data.email)) {
        errors.push('Please enter a valid email address');
    }
    
    if (!data.subject || data.subject.trim().length < 3) {
        errors.push('Subject must be at least 3 characters long');
    }
    
    if (!data.message || data.message.trim().length < 10) {
        errors.push('Message must be at least 10 characters long');
    }
    
    if (errors.length > 0) {
        showFormErrors(errors);
        return false;
    }
    
    return true;
}

// Check if email is valid
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Show form validation errors
function showFormErrors(errors) {
    // Remove existing error messages
    const existingErrors = document.querySelectorAll('.form-error');
    existingErrors.forEach(error => error.remove());
    
    // Create error container
    const errorContainer = document.createElement('div');
    errorContainer.className = 'form-error';
    errorContainer.style.cssText = `
        background: #fee2e2;
        border: 1px solid #fecaca;
        color: #dc2626;
        padding: 1rem;
        border-radius: 8px;
        margin-bottom: 1rem;
    `;
    
    // Add error messages
    const errorList = document.createElement('ul');
    errorList.style.cssText = 'margin: 0; padding-left: 1.5rem;';
    
    errors.forEach(error => {
        const errorItem = document.createElement('li');
        errorItem.textContent = error;
        errorList.appendChild(errorItem);
    });
    
    errorContainer.appendChild(errorList);
    
    // Insert error container at the top of the form
    const contactForm = document.getElementById('contact-form');
    contactForm.insertBefore(errorContainer, contactForm.firstChild);
    
    // Scroll to error
    errorContainer.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

// Simulate form submission
function submitContactForm(data) {
    const submitButton = document.querySelector('#contact-form button[type="submit"]');
    const originalText = submitButton.innerHTML;
    
    // Show loading state
    submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Sending...';
    submitButton.disabled = true;
    
    // Simulate API call
    setTimeout(() => {
        // Show success message
        showFormSuccess();
        
        // Reset form
        document.getElementById('contact-form').reset();
        
        // Reset button
        submitButton.innerHTML = originalText;
        submitButton.disabled = false;
        
        // Log form data (in a real app, this would be sent to a server)
        console.log('Contact form submitted:', data);
    }, 2000);
}

// Show form success message
function showFormSuccess() {
    // Remove existing messages
    const existingMessages = document.querySelectorAll('.form-error, .form-success');
    existingMessages.forEach(message => message.remove());
    
    // Create success container
    const successContainer = document.createElement('div');
    successContainer.className = 'form-success';
    successContainer.style.cssText = `
        background: #dcfce7;
        border: 1px solid #bbf7d0;
        color: #16a34a;
        padding: 1rem;
        border-radius: 8px;
        margin-bottom: 1rem;
        text-align: center;
    `;
    
    successContainer.innerHTML = `
        <i class="fas fa-check-circle" style="margin-right: 0.5rem;"></i>
        Thank you for your message! We'll get back to you soon.
    `;
    
    // Insert success container at the top of the form
    const contactForm = document.getElementById('contact-form');
    contactForm.insertBefore(successContainer, contactForm.firstChild);
    
    // Remove success message after 5 seconds
    setTimeout(() => {
        successContainer.remove();
    }, 5000);
    
    // Scroll to success message
    successContainer.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

// Utility functions
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

// Performance optimizations
const debouncedScroll = debounce(highlightActiveNavLink, 10);
window.addEventListener('scroll', debouncedScroll);

// Lazy loading for images
function initializeLazyLoading() {
    const images = document.querySelectorAll('img[data-src]');
    
    const imageObserver = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                img.classList.remove('lazy');
                imageObserver.unobserve(img);
            }
        });
    });

    images.forEach(img => {
        imageObserver.observe(img);
    });
}

// Initialize lazy loading if images have data-src attributes
document.addEventListener('DOMContentLoaded', initializeLazyLoading);

// Add loading animation
function showPageLoader() {
    const loader = document.createElement('div');
    loader.id = 'page-loader';
    loader.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: #ffffff;
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 9999;
        transition: opacity 0.3s ease;
    `;
    
    loader.innerHTML = `
        <div style="text-align: center;">
            <div style="width: 50px; height: 50px; border: 3px solid #e5e7eb; border-top: 3px solid #6366f1; border-radius: 50%; animation: spin 1s linear infinite; margin: 0 auto 1rem;"></div>
            <p style="color: #6b7280; font-size: 1.1rem;">Loading...</p>
        </div>
    `;
    
    // Add spinner animation
    const style = document.createElement('style');
    style.textContent = `
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    `;
    document.head.appendChild(style);
    
    document.body.appendChild(loader);
    
    // Hide loader when page is fully loaded
    window.addEventListener('load', function() {
        setTimeout(() => {
            loader.style.opacity = '0';
            setTimeout(() => {
                loader.remove();
            }, 300);
        }, 500);
    });
}

// Initialize page loader
showPageLoader();

// Error handling for images
document.addEventListener('error', function(e) {
    if (e.target.tagName === 'IMG') {
        e.target.style.display = 'none';
        console.warn('Failed to load image:', e.target.src);
    }
}, true);

// Add click tracking for analytics (placeholder)
function trackClick(element, action) {
    // This would integrate with your analytics service
    console.log('Click tracked:', {
        element: element.tagName,
        action: action,
        timestamp: new Date().toISOString()
    });
}

// Track download button clicks
document.addEventListener('click', function(e) {
    if (e.target.closest('.download-badge')) {
        trackClick(e.target, 'download_button_click');
    }
    if (e.target.closest('.btn-primary')) {
        trackClick(e.target, 'primary_button_click');
    }
});
