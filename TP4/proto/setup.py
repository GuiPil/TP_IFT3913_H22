from setuptools import setup, find_packages


setup(
    name="proto",
    version="1.0.0",
    url="https://github.com/GuiPil/TP_IFT3913_H22/tree/main/TP4",
    author="Jérémie Tousignant et Guillaume Pilon",
    python_requires=">=3.8",
    scripts=['main.py'],
    install_requires=[
        "pandas>=1.4.0",
        "tqdm>=4.30.0"
    ],
    entry_points={
        'console_scripts': ['proto = main:main'],
    }
)