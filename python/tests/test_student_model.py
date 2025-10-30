"""
Unit tests for Student model.
Tests model methods, validation, and serialization.
"""
import pytest
from datetime import datetime
from app import app, db
from models.student import Student


@pytest.fixture
def client():
    """Create a test client."""
    app.config['TESTING'] = True
    app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///:memory:'
    
    with app.test_client() as client:
        with app.app_context():
            db.create_all()
            yield client
            db.drop_all()


@pytest.fixture
def sample_student():
    """Create a sample student for testing."""
    return Student(
        name="John Doe",
        email="john@example.com",
        major="Computer Science",
        gpa=3.8,
        is_active=True
    )


class TestStudentModel:
    """Test cases for Student model"""
    
    def test_student_creation(self, client, sample_student):
        """Test creating a student"""
        with app.app_context():
            db.session.add(sample_student)
            db.session.commit()
            
            student = Student.query.filter_by(email="john@example.com").first()
            assert student is not None
            assert student.name == "John Doe"
            assert student.email == "john@example.com"
            assert student.major == "Computer Science"
            assert student.gpa == 3.8
            assert student.is_active is True
    
    def test_student_repr(self, sample_student):
        """Test student string representation"""
        assert repr(sample_student) == '<Student John Doe>'
    
    def test_to_dict(self, sample_student):
        """Test converting student to dictionary"""
        data = sample_student.to_dict()
        
        assert isinstance(data, dict)
        assert data['name'] == "John Doe"
        assert data['email'] == "john@example.com"
        assert data['major'] == "Computer Science"
        assert data['gpa'] == 3.8
        assert data['is_active'] is True
    
    def test_from_dict(self):
        """Test creating student from dictionary"""
        data = {
            'name': 'Jane Smith',
            'email': 'jane@example.com',
            'major': 'Mathematics',
            'gpa': 3.5,
            'is_active': True
        }
        
        student = Student.from_dict(data)
        
        assert student.name == 'Jane Smith'
        assert student.email == 'jane@example.com'
        assert student.major == 'Mathematics'
        assert student.gpa == 3.5
        assert student.is_active is True
    
    def test_from_dict_defaults(self):
        """Test creating student from dictionary with default values"""
        data = {
            'name': 'Bob Johnson',
            'email': 'bob@example.com',
            'major': 'Physics'
        }
        
        student = Student.from_dict(data)
        
        assert student.gpa == 0.0
        assert student.is_active is True
    
    def test_unique_email_constraint(self, client):
        """Test that email must be unique"""
        with app.app_context():
            student1 = Student(
                name="Student 1",
                email="duplicate@example.com",
                major="CS",
                gpa=3.0
            )
            student2 = Student(
                name="Student 2",
                email="duplicate@example.com",
                major="Math",
                gpa=3.5
            )
            
            db.session.add(student1)
            db.session.commit()
            
            db.session.add(student2)
            with pytest.raises(Exception):  # SQLAlchemy will raise IntegrityError
                db.session.commit()
    
    def test_default_values(self, client):
        """Test default values for student fields"""
        with app.app_context():
            student = Student(
                name="Test Student",
                email="test@example.com",
                major="Engineering"
            )
            db.session.add(student)
            db.session.commit()
            
            retrieved = Student.query.filter_by(email="test@example.com").first()
            assert retrieved.gpa == 0.0
            assert retrieved.is_active is True
            assert retrieved.created_at is not None
            assert retrieved.updated_at is not None
